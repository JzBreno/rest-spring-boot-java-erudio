import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface PanelTab {
  id: string;
  label: string;
  method?: string;
}

@Component({
  selector: 'app-panel-tabs',
  template: `
    <div class="panel-tabs" role="tablist">
      @for (tab of tabs; track tab.id) {
        <button
          type="button"
          role="tab"
          class="panel-tab"
          [class.active]="tab.id === activeId"
          [attr.aria-selected]="tab.id === activeId"
          (click)="tabChange.emit(tab.id)"
        >
          @if (tab.method) {
            <span class="method" [attr.data-method]="tab.method">{{ tab.method }}</span>
          }
          {{ tab.label }}
        </button>
      }
    </div>
  `,
  styleUrl: './panel-tabs.component.css',
})
export class PanelTabsComponent {
  @Input({ required: true }) tabs: PanelTab[] = [];
  @Input({ required: true }) activeId = '';
  @Output() tabChange = new EventEmitter<string>();
}
