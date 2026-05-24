import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PcService } from '../../../core/services/pc.service';
import { Pc } from '../../../models/pc.model';
import { PanelTab, PanelTabsComponent } from '../../../shared/panel-tabs/panel-tabs.component';
import { ResponseBoxComponent } from '../../../shared/response-box/response-box.component';

@Component({
  selector: 'app-pc-panel',
  imports: [ReactiveFormsModule, PanelTabsComponent, ResponseBoxComponent],
  templateUrl: './pc-panel.component.html',
})
export class PcPanelComponent {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(PcService);

  activeTab = 'listar';
  loading = false;
  error = '';
  success = '';
  data: unknown = null;

  readonly tabs: PanelTab[] = [
    { id: 'listar', label: 'Listar', method: 'GET' },
    { id: 'buscar', label: 'Buscar ID', method: 'GET' },
    { id: 'criar', label: 'Criar', method: 'POST' },
    { id: 'atualizar', label: 'Atualizar', method: 'PUT' },
    { id: 'excluir', label: 'Excluir', method: 'DELETE' },
  ];

  idForm = this.fb.nonNullable.group({ id: ['', [Validators.required, Validators.pattern(/^\d+$/)]] });
  pcForm = this.fb.nonNullable.group({
    id: [0],
    video_card: ['', Validators.required],
    cpu: ['', Validators.required],
    ramMemory: ['', Validators.required],
    storage_unit: ['', Validators.required],
  });

  onTab(id: string): void {
    this.activeTab = id;
    this.error = this.success = '';
    this.data = null;
  }

  listar(): void {
    this.exec(() => this.api.findAll());
  }
  buscar(): void {
    if (this.idForm.invalid) return;
    this.exec(() => this.api.findById(+this.idForm.controls.id.value));
  }
  criar(): void {
    if (this.pcForm.invalid) return;
    const { id, ...body } = this.pcForm.getRawValue();
    this.exec(() => this.api.create(body as Pc), 'PC criado.');
  }
  atualizar(): void {
    if (this.pcForm.invalid) return;
    this.exec(() => this.api.update(this.pcForm.getRawValue() as Pc), 'PC atualizado.');
  }
  excluir(): void {
    if (this.idForm.invalid) return;
    const id = +this.idForm.controls.id.value;
    if (!confirm(`Excluir PC ${id}?`)) return;
    this.exec(() => this.api.delete(id), 'PC excluído.');
  }
  carregar(): void {
    if (this.idForm.invalid) return;
    this.exec(() => this.api.findById(+this.idForm.controls.id.value), '', (p: Pc) => this.pcForm.patchValue(p));
  }

  private exec<T>(fn: () => import('rxjs').Observable<T>, msg = '', cb?: (v: T) => void): void {
    this.loading = true;
    this.error = this.success = '';
    this.data = null;
    fn().subscribe({
      next: (r) => {
        if (cb) cb(r);
        else this.data = r;
        if (msg) this.success = msg;
        this.loading = false;
      },
      error: (e: Error) => {
        this.error = e.message;
        this.loading = false;
      },
    });
  }
}
