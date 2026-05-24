import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PanelTab, PanelTabsComponent } from '../../../shared/panel-tabs/panel-tabs.component';
import { ResponseBoxComponent } from '../../../shared/response-box/response-box.component';
import { VeiculoService } from '../../../core/services/veiculo.service';
import { Veiculo } from '../../../models/veiculo.model';

@Component({
  selector: 'app-veiculo-panel',
  imports: [ReactiveFormsModule, PanelTabsComponent, ResponseBoxComponent],
  templateUrl: './veiculo-panel.component.html',
})
export class VeiculoPanelComponent {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(VeiculoService);

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
  veiculoForm = this.fb.nonNullable.group({
    id: [{ value: 0, disabled: true }],
    placa: ['', Validators.required],
    marca: ['', Validators.required],
    modelo: ['', Validators.required],
    ano: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
    cor: ['', Validators.required],
  });
  createForm = this.fb.nonNullable.group({
    placa: ['', Validators.required],
    marca: ['', Validators.required],
    modelo: ['', Validators.required],
    ano: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
    cor: ['', Validators.required],
  });

  onTab(id: string): void {
    this.activeTab = id;
    this.resetState();
  }

  listar(): void {
    this.run(() => this.api.findAll());
  }

  buscar(): void {
    if (this.idForm.invalid) return this.idForm.markAllAsTouched();
    this.run(() => this.api.findById(Number(this.idForm.controls.id.value)));
  }

  criar(): void {
    if (this.createForm.invalid) return this.createForm.markAllAsTouched();
    this.run(() => this.api.create(this.createForm.getRawValue()), 'Veículo cadastrado.');
  }

  carregarEdicao(): void {
    if (this.idForm.invalid) return this.idForm.markAllAsTouched();
    this.run(() => this.api.findById(Number(this.idForm.controls.id.value)), '', (v: Veiculo) => {
      this.veiculoForm.patchValue({ id: v.id!, placa: v.placa, marca: v.marca, modelo: v.modelo, ano: v.ano, cor: v.cor });
    });
  }

  atualizar(): void {
    if (this.veiculoForm.invalid) return this.veiculoForm.markAllAsTouched();
    this.run(() => this.api.update(this.veiculoForm.getRawValue() as Veiculo), 'Veículo atualizado.');
  }

  excluir(): void {
    if (this.idForm.invalid) return this.idForm.markAllAsTouched();
    const id = Number(this.idForm.controls.id.value);
    if (!confirm(`Excluir veículo ${id}?`)) return;
    this.run(() => this.api.delete(id), 'Veículo excluído.');
  }

  private run<T>(call: () => import('rxjs').Observable<T>, successMsg = '', onOk?: (v: T) => void): void {
    this.loading = true;
    this.error = '';
    this.success = '';
    this.data = null;
    call().subscribe({
      next: (res) => {
        if (onOk) onOk(res);
        else this.data = res;
        if (successMsg) this.success = successMsg;
        this.loading = false;
      },
      error: (e: Error) => {
        this.error = e.message;
        this.loading = false;
      },
    });
  }

  private resetState(): void {
    this.error = '';
    this.success = '';
    this.data = null;
  }
}
