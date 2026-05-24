import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';
import { Greeting } from '../../models/greeting.model';

@Injectable({ providedIn: 'root' })
export class GreetingService {
  private readonly http = inject(HttpClient);

  get(name = 'world!'): Observable<Greeting> {
    const params = new HttpParams().set('name', name);
    return this.http
      .get<Greeting>(`${environment.apiBaseUrl}/greeting`, { params })
      .pipe(catchError(handleApiError));
  }
}
