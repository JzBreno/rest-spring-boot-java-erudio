import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PersonV2Service } from '../../../core/services/person-v2.service';
import { PageParams, PersonV2 } from '../../../models/person.model';
import { downloadBlob } from '../../../core/utils/download.util';
import { PanelTab, PanelTabsComponent } from '../../../shared/panel-tabs/panel-tabs.component';
import { ResponseBoxComponent } from '../../../shared/response-box/response-box.component';

@Component({
  selector: 'app-person-v2-panel',
  imports: [ReactiveFormsModule, PanelTabsComponent, ResponseBoxComponent],
  templateUrl: './person-v2-panel.component.html',
})
export class PersonV2PanelComponent {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(PersonV2Service);

  activeTab = 'listar';
  loading = false;
  error = '';
  success = '';
  data: unknown = null;
  selectedFile: File | null = null;

  readonly tabs: PanelTab[] = [
    { id: 'listar', label: 'Listar', method: 'GET' },
    { id: 'buscar', label: 'Buscar ID', method: 'GET' },
    { id: 'nome', label: 'Por nome', method: 'GET' },
    { id: 'criar', label: 'Criar', method: 'POST' },
    { id: 'atualizar', label: 'Atualizar', method: 'PUT' },
    { id: 'excluir', label: 'Excluir', method: 'DELETE' },
    { id: 'mass', label: 'Mass create', method: 'POST' },
    { id: 'export', label: 'Exportar', method: 'GET' },
  ];

  pageForm = this.fb.nonNullable.group({
    page: [0],
    size: [15],
    direction: ['asc'],
    sort: ['firstName'],
  });
  idForm = this.fb.nonNullable.group({ id: ['', Validators.required] });
  nameForm = this.fb.nonNullable.group({ name: ['', Validators.required] });
  personForm = this.fb.nonNullable.group({
    id: [0],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    birthday: [''],
    address: [''],
    gender: [''],
    enabled: [true],
  });
  exportForm = this.fb.nonNullable.group({
    accept: ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'],
  });

  onTab(id: string): void {
    this.activeTab = id;
    this.error = this.success = '';
    this.data = null;
  }

  pageParams(): PageParams {
    const v = this.pageForm.getRawValue();
    return { page: v.page, size: v.size, direction: v.direction, sort: v.sort };
  }

  listar(): void {
    this.exec(() => this.api.findAll(this.pageParams()));
  }
  buscar(): void {
    if (this.idForm.invalid) return;
    this.exec(() => this.api.findById(this.idForm.controls.id.value));
  }
  porNome(): void {
    if (this.nameForm.invalid) return;
    this.exec(() => this.api.findByName(this.nameForm.controls.name.value, this.pageParams()));
  }
  criar(): void {
    if (this.personForm.invalid) return;
    const { id, ...body } = this.personForm.getRawValue();
    this.exec(() => this.api.create(body as PersonV2), 'Pessoa criada.');
  }
  atualizar(): void {
    if (this.personForm.invalid) return;
    this.exec(() => this.api.update(this.personForm.getRawValue() as PersonV2), 'Pessoa atualizada.');
  }
  excluir(): void {
    if (this.idForm.invalid) return;
    if (!confirm('Excluir pessoa?')) return;
    this.exec(() => this.api.delete(this.idForm.controls.id.value), 'Pessoa excluída.');
  }
  onFile(e: Event): void {
    this.selectedFile = (e.target as HTMLInputElement).files?.[0] ?? null;
  }
  massCreate(): void {
    if (!this.selectedFile) {
      this.error = 'Selecione um arquivo.';
      return;
    }
    this.exec(() => this.api.massCreate(this.selectedFile!), 'Importação concluída.');
  }
  exportar(): void {
    this.loading = true;
    this.api.exportPage(this.pageParams(), this.exportForm.controls.accept.value).subscribe({
      next: (blob) => {
        const accept = this.exportForm.controls.accept.value;
        const ext = accept.includes('pdf') ? '.pdf' : accept.includes('csv') ? '.csv' : '.xlsx';
        downloadBlob(blob, `people_v2_export${ext}`);
        this.success = 'Arquivo baixado.';
        this.loading = false;
      },
      error: (e: Error) => {
        this.error = e.message;
        this.loading = false;
      },
    });
  }
  carregar(): void {
    if (this.idForm.invalid) return;
    this.exec(() => this.api.findById(this.idForm.controls.id.value), '', (p: unknown) => {
      const body = p as PersonV2 & { firstName?: string };
      this.personForm.patchValue(body);
    });
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
