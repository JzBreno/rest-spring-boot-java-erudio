import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { JasperService } from '../../../core/services/jasper.service';
import { downloadBlob } from '../../../core/utils/download.util';
import { PanelTab, PanelTabsComponent } from '../../../shared/panel-tabs/panel-tabs.component';
import { ResponseBoxComponent } from '../../../shared/response-box/response-box.component';

@Component({
  selector: 'app-jasper-panel',
  imports: [ReactiveFormsModule, PanelTabsComponent, ResponseBoxComponent],
  templateUrl: './jasper-panel.component.html',
})
export class JasperPanelComponent {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(JasperService);

  activeTab = 'pdf';
  loading = false;
  error = '';
  success = '';

  readonly tabs: PanelTab[] = [
    { id: 'pdf', label: 'PDF', method: 'GET' },
    { id: 'xlsx', label: 'XLSX', method: 'GET' },
  ];

  pageForm = this.fb.nonNullable.group({
    page: [0],
    size: [15],
    direction: ['asc'],
    sort: ['firstName'],
  });

  onTab(id: string): void {
    this.activeTab = id;
    this.error = this.success = '';
  }

  exportar(): void {
    this.loading = true;
    this.error = '';
    const p = this.pageForm.getRawValue();
    const call = this.activeTab === 'pdf' ? this.api.exportPdf(p) : this.api.exportXlsx(p);
    call.subscribe({
      next: (blob) => {
        downloadBlob(blob, `jasper_report.${this.activeTab}`);
        this.success = 'Relatório baixado.';
        this.loading = false;
      },
      error: (e: Error) => {
        this.error = e.message;
        this.loading = false;
      },
    });
  }
}
