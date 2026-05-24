import { Component, Input } from '@angular/core';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'app-response-box',
  imports: [JsonPipe],
  template: `
    @if (loading) {
      <p class="loading">Processando requisição...</p>
    }
    @if (error) {
      <p class="alert alert-error">{{ error }}</p>
    }
    @if (success) {
      <p class="alert alert-success">{{ success }}</p>
    }
    @if (data !== null && data !== undefined) {
      <pre class="response-pre">{{ data | json }}</pre>
    }
  `,
  styleUrl: './response-box.component.css',
})
export class ResponseBoxComponent {
  @Input() loading = false;
  @Input() error = '';
  @Input() success = '';
  @Input() data: unknown = null;
}
