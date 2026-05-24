import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';
import { Veiculo } from '../../models/veiculo.model';

@Injectable({ providedIn: 'root' })
export class VeiculoService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiBaseUrl}/veiculo`;

  findById(id: number): Observable<Veiculo> {
    return this.http.get<Veiculo>(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }

  findAll(): Observable<Veiculo[]> {
    return this.http.get<Veiculo[]>(`${this.base}/findAll`, { observe: 'response' }).pipe(
      map((r) => (r.status === 204 ? [] : r.body ?? [])),
      catchError(handleApiError),
    );
  }

  create(veiculo: Omit<Veiculo, 'id'>): Observable<Veiculo> {
    return this.http.post<Veiculo>(`${this.base}/create`, veiculo).pipe(catchError(handleApiError));
  }

  update(veiculo: Veiculo): Observable<Veiculo> {
    return this.http.put<Veiculo>(`${this.base}/update`, veiculo).pipe(catchError(handleApiError));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }
}
