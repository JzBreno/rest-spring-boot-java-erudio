import { Component } from '@angular/core';
import { FilesPanelComponent } from '../files-panel/files-panel.component';

@Component({
  selector: 'app-files-v2-panel',
  imports: [FilesPanelComponent],
  template: '<app-files-panel version="v2" />',
})
export class FilesV2PanelComponent {}
