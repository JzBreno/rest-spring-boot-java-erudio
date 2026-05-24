import { Component, Input, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { FileApiService } from '../../../core/services/file-api.service';
import { downloadBlob } from '../../../core/utils/download.util';
import { PanelTab, PanelTabsComponent } from '../../../shared/panel-tabs/panel-tabs.component';
import { ResponseBoxComponent } from '../../../shared/response-box/response-box.component';

@Component({
  selector: 'app-files-panel',
  imports: [ReactiveFormsModule, PanelTabsComponent, ResponseBoxComponent],
  templateUrl: './files-panel.component.html',
})
export class FilesPanelComponent {
  @Input({ required: true }) version: 'v1' | 'v2' = 'v1';

  private readonly fb = inject(FormBuilder);
  private readonly api = inject(FileApiService);

  activeTab = 'upload';
  loading = false;
  error = '';
  success = '';
  data: unknown = null;
  singleFile: File | null = null;
  multiFiles: File[] = [];

  readonly tabs: PanelTab[] = [
    { id: 'upload', label: 'Upload', method: 'POST' },
    { id: 'uploads', label: 'Upload múltiplo', method: 'POST' },
    { id: 'download', label: 'Download', method: 'GET' },
  ];

  downloadForm = this.fb.nonNullable.group({ fileName: ['', Validators.required] });

  get basePath(): string {
    return `/api/files/${this.version}`;
  }

  onTab(id: string): void {
    this.activeTab = id;
    this.error = this.success = '';
    this.data = null;
  }

  onSingle(e: Event): void {
    this.singleFile = (e.target as HTMLInputElement).files?.[0] ?? null;
  }
  onMulti(e: Event): void {
    this.multiFiles = Array.from((e.target as HTMLInputElement).files ?? []);
  }

  upload(): void {
    if (!this.singleFile) {
      this.error = 'Selecione um arquivo.';
      return;
    }
    this.exec(() => this.api.uploadFile(this.version, this.singleFile!), 'Upload concluído.');
  }

  uploadMany(): void {
    if (!this.multiFiles.length) {
      this.error = 'Selecione arquivos.';
      return;
    }
    this.exec(() => this.api.uploadFiles(this.version, this.multiFiles), 'Uploads concluídos.');
  }

  download(): void {
    if (this.downloadForm.invalid) return;
    this.loading = true;
    this.error = '';
    const name = this.downloadForm.controls.fileName.value;
    this.api.download(this.version, name).subscribe({
      next: (blob) => {
        downloadBlob(blob, name);
        this.success = 'Download iniciado.';
        this.loading = false;
      },
      error: (e: Error) => {
        this.error = e.message;
        this.loading = false;
      },
    });
  }

  private exec<T>(fn: () => import('rxjs').Observable<T>, msg = ''): void {
    this.loading = true;
    this.error = this.success = '';
    this.data = null;
    fn().subscribe({
      next: (r) => {
        this.data = r;
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
