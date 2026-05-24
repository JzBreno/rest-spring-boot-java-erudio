import { Component } from '@angular/core';
import { FilesPanelComponent } from '../files-panel/files-panel.component';

@Component({
  selector: 'app-files-v1-panel',
  imports: [FilesPanelComponent],
  template: '<app-files-panel version="v1" />',
})
export class FilesV1PanelComponent {}
