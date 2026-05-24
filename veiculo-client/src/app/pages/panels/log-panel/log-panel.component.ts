import { Component, inject } from '@angular/core';
import { LogTestService } from '../../../core/services/log-test.service';
import { ResponseBoxComponent } from '../../../shared/response-box/response-box.component';

@Component({
  selector: 'app-log-panel',
  imports: [ResponseBoxComponent],
  templateUrl: './log-panel.component.html',
})
export class LogPanelComponent {
  private readonly api = inject(LogTestService);

  loading = false;
  error = '';
  data: unknown = null;

  test(): void {
    this.loading = true;
    this.error = '';
    this.data = null;
    this.api.test().subscribe({
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
