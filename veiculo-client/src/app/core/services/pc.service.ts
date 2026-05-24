import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';
import { Pc } from '../../models/pc.model';

@Injectable({ providedIn: 'root' })
export class PcService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiBaseUrl}/pc`;

  findById(id: number): Observable<Pc> {
    return this.http.get<Pc>(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }

  findAll(): Observable<Pc[]> {
    return this.http.get<Pc[]>(`${this.base}/findAll`).pipe(catchError(handleApiError));
  }

  create(pc: Pc): Observable<Pc> {
    return this.http.post<Pc>(`${this.base}`, pc).pipe(catchError(handleApiError));
  }

  update(pc: Pc): Observable<Pc> {
    return this.http.put<Pc>(`${this.base}/${pc.id}`, pc).pipe(catchError(handleApiError));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }
}
