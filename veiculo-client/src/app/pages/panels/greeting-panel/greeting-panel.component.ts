import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { GreetingService } from '../../../core/services/greeting.service';
import { ResponseBoxComponent } from '../../../shared/response-box/response-box.component';

@Component({
  selector: 'app-greeting-panel',
  imports: [ReactiveFormsModule, ResponseBoxComponent],
  templateUrl: './greeting-panel.component.html',
})
export class GreetingPanelComponent {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(GreetingService);

  loading = false;
  error = '';
  data: unknown = null;

  form = this.fb.nonNullable.group({ name: ['world!'] });

  greet(): void {
    this.loading = true;
    this.error = '';
    this.data = null;
    this.api.get(this.form.controls.name.value).subscribe({
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
