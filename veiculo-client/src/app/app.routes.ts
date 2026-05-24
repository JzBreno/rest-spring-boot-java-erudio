import { Routes } from '@angular/router';
import { MainShellComponent } from './layout/main-shell/main-shell.component';
import { HomeComponent } from './pages/home/home.component';
import { VeiculoPanelComponent } from './pages/panels/veiculo-panel/veiculo-panel.component';
import { BookPanelComponent } from './pages/panels/book-panel/book-panel.component';
import { PcPanelComponent } from './pages/panels/pc-panel/pc-panel.component';
import { PersonV1PanelComponent } from './pages/panels/person-v1-panel/person-v1-panel.component';
import { PersonV2PanelComponent } from './pages/panels/person-v2-panel/person-v2-panel.component';
import { FilesV1PanelComponent } from './pages/panels/files-v1-panel/files-v1-panel.component';
import { FilesV2PanelComponent } from './pages/panels/files-v2-panel/files-v2-panel.component';
import { JasperPanelComponent } from './pages/panels/jasper-panel/jasper-panel.component';
import { MathPanelComponent } from './pages/panels/math-panel/math-panel.component';
import { GreetingPanelComponent } from './pages/panels/greeting-panel/greeting-panel.component';
import { LogPanelComponent } from './pages/panels/log-panel/log-panel.component';

export const routes: Routes = [
  {
    path: '',
    component: MainShellComponent,
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent, title: 'Início | REST Client' },
      { path: 'api/veiculo', component: VeiculoPanelComponent, title: 'Veículo | REST Client' },
      { path: 'api/book', component: BookPanelComponent, title: 'Book | REST Client' },
      { path: 'api/pc', component: PcPanelComponent, title: 'PC | REST Client' },
      { path: 'api/person-v1', component: PersonV1PanelComponent, title: 'Person v1 | REST Client' },
      { path: 'api/person-v2', component: PersonV2PanelComponent, title: 'Person v2 | REST Client' },
      { path: 'api/files-v1', component: FilesV1PanelComponent, title: 'Files v1 | REST Client' },
      { path: 'api/files-v2', component: FilesV2PanelComponent, title: 'Files v2 | REST Client' },
      { path: 'api/jasper', component: JasperPanelComponent, title: 'Jasper | REST Client' },
      { path: 'api/math', component: MathPanelComponent, title: 'Math | REST Client' },
      { path: 'api/greeting', component: GreetingPanelComponent, title: 'Greeting | REST Client' },
      { path: 'api/log', component: LogPanelComponent, title: 'Log | REST Client' },
    ],
  },
  { path: '**', redirectTo: 'home' },
];
