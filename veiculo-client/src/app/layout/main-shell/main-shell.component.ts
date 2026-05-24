import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { API_CONTROLLERS } from '../../core/config/api-controllers.config';

@Component({
  selector: 'app-main-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './main-shell.component.html',
  styleUrl: './main-shell.component.css',
})
export class MainShellComponent {
  readonly controllers = API_CONTROLLERS;
}
