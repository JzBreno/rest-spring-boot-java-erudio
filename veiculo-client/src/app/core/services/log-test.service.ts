import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';

@Injectable({ providedIn: 'root' })
export class LogTestService {
  private readonly http = inject(HttpClient);

  test(): Observable<string> {
    return this.http
      .get(`${environment.apiBaseUrl}/log/test`, { responseType: 'text' })
      .pipe(catchError(handleApiError));
  }
}
