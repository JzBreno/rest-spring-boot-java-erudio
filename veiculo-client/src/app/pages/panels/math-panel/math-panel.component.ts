import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MathService } from '../../../core/services/math.service';
import { PanelTab, PanelTabsComponent } from '../../../shared/panel-tabs/panel-tabs.component';
import { ResponseBoxComponent } from '../../../shared/response-box/response-box.component';

@Component({
  selector: 'app-math-panel',
  imports: [ReactiveFormsModule, PanelTabsComponent, ResponseBoxComponent],
  templateUrl: './math-panel.component.html',
})
export class MathPanelComponent {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(MathService);

  activeTab = 'sum';
  loading = false;
  error = '';
  data: unknown = null;

  readonly tabs: PanelTab[] = [
    { id: 'sum', label: 'Soma', method: 'GET' },
    { id: 'sub', label: 'Subtração', method: 'GET' },
    { id: 'div', label: 'Divisão', method: 'GET' },
    { id: 'mul', label: 'Multiplicação', method: 'GET' },
    { id: 'square', label: 'Raiz', method: 'GET' },
  ];

  twoForm = this.fb.nonNullable.group({
    a: ['', Validators.required],
    b: ['', Validators.required],
  });
  oneForm = this.fb.nonNullable.group({ a: ['', Validators.required] });

  onTab(id: string): void {
    this.activeTab = id;
    this.error = '';
    this.data = null;
  }

  calc(): void {
    const isSquare = this.activeTab === 'square';
    if (isSquare ? this.oneForm.invalid : this.twoForm.invalid) return;

    const a = isSquare ? this.oneForm.controls.a.value : this.twoForm.controls.a.value;
    const b = this.twoForm.controls.b.value;

    const calls = {
      sum: () => this.api.sum(a, b),
      sub: () => this.api.sub(a, b),
      div: () => this.api.div(a, b),
      mul: () => this.api.mul(a, b),
      square: () => this.api.square(a),
    } as const;

    this.loading = true;
    calls[this.activeTab as keyof typeof calls]().subscribe({
      next: (r) => {
        this.data = r;
        this.loading = false;
      },
      error: (e: Error) => {
        this.error = e.message;
        this.loading = false;
      },
    });
  }
}
