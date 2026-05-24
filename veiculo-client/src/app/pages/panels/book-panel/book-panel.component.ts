import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BookService } from '../../../core/services/book.service';
import { Book } from '../../../models/book.model';
import { PanelTab, PanelTabsComponent } from '../../../shared/panel-tabs/panel-tabs.component';
import { ResponseBoxComponent } from '../../../shared/response-box/response-box.component';

@Component({
  selector: 'app-book-panel',
  imports: [ReactiveFormsModule, PanelTabsComponent, ResponseBoxComponent],
  templateUrl: './book-panel.component.html',
})
export class BookPanelComponent {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(BookService);

  activeTab = 'listar';
  loading = false;
  error = '';
  success = '';
  data: unknown = null;

  readonly tabs: PanelTab[] = [
    { id: 'listar', label: 'Listar', method: 'GET' },
    { id: 'buscar', label: 'Buscar ID', method: 'GET' },
    { id: 'criar', label: 'Criar', method: 'POST' },
    { id: 'save', label: 'Save', method: 'POST' },
    { id: 'atualizar', label: 'Atualizar', method: 'PUT' },
    { id: 'excluir', label: 'Excluir', method: 'DELETE' },
  ];

  idForm = this.fb.nonNullable.group({ id: ['', [Validators.required, Validators.pattern(/^\d+$/)]] });
  bookForm = this.fb.nonNullable.group({
    id: [0],
    title: ['', Validators.required],
    author: ['', Validators.required],
    lauch_date: [''],
    price: [''],
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
    if (this.bookForm.invalid) return;
    const { id, ...body } = this.bookForm.getRawValue();
    this.exec(() => this.api.create(body as Book), 'Livro criado (201).');
  }
  save(): void {
    if (this.bookForm.invalid) return;
    this.exec(() => this.api.save(this.bookForm.getRawValue() as Book), 'Livro salvo.');
  }
  atualizar(): void {
    if (this.bookForm.invalid) return;
    this.exec(() => this.api.update(this.bookForm.getRawValue() as Book), 'Livro atualizado.');
  }
  excluir(): void {
    if (this.idForm.invalid) return;
    const id = +this.idForm.controls.id.value;
    if (!confirm(`Excluir livro ${id}?`)) return;
    this.exec(() => this.api.delete(id), 'Livro excluído.');
  }
  carregar(): void {
    if (this.idForm.invalid) return;
    this.exec(() => this.api.findById(+this.idForm.controls.id.value), '', (b: Book) =>
      this.bookForm.patchValue(b),
    );
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
