import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';
import { MathResult } from '../../models/math.model';

@Injectable({ providedIn: 'root' })
export class MathService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiBaseUrl}/math`;

  sum(a: string, b: string): Observable<MathResult> {
    return this.http.get<MathResult>(`${this.base}/sum/${a}/${b}`).pipe(catchError(handleApiError));
  }

  sub(a: string, b: string): Observable<MathResult> {
    return this.http.get<MathResult>(`${this.base}/sub/${a}/${b}`).pipe(catchError(handleApiError));
  }

  div(a: string, b: string): Observable<MathResult> {
    return this.http.get<MathResult>(`${this.base}/div/${a}/${b}`).pipe(catchError(handleApiError));
  }

  mul(a: string, b: string): Observable<MathResult> {
    return this.http.get<MathResult>(`${this.base}/mul/${a}/${b}`).pipe(catchError(handleApiError));
  }

  square(a: string): Observable<MathResult> {
    return this.http.get<MathResult>(`${this.base}/square/${a}`).pipe(catchError(handleApiError));
  }
}
