import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { API_CONTROLLERS } from '../../core/config/api-controllers.config';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  readonly controllers = API_CONTROLLERS;
}
