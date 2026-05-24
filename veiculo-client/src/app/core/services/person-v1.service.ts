import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';
import { PageParams, PersonV1 } from '../../models/person.model';

@Injectable({ providedIn: 'root' })
export class PersonV1Service {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiBaseUrl}/person/v1`;

  private pageParams(p: PageParams): HttpParams {
    return new HttpParams()
      .set('page', String(p.page ?? 0))
      .set('size', String(p.size ?? 15))
      .set('direction', p.direction ?? 'asc')
      .set('properties', p.properties ?? 'firstName');
  }

  findById(id: string): Observable<PersonV1> {
    return this.http.get<PersonV1>(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }

  findAll(p: PageParams = {}): Observable<unknown> {
    return this.http
      .get(`${this.base}/findAll`, { params: this.pageParams(p), observe: 'response' })
      .pipe(
        map((r: HttpResponse<unknown>) => (r.status === 204 ? { content: [] } : r.body)),
        catchError(handleApiError),
      );
  }

  findByName(name: string, p: PageParams = {}): Observable<unknown> {
    return this.http
      .get(`${this.base}/findByName/${encodeURIComponent(name)}`, {
        params: this.pageParams(p),
        observe: 'response',
      })
      .pipe(
        map((r: HttpResponse<unknown>) => (r.status === 204 ? { content: [] } : r.body)),
        catchError(handleApiError),
      );
  }

  create(person: PersonV1): Observable<PersonV1> {
    return this.http.post<PersonV1>(`${this.base}`, person).pipe(catchError(handleApiError));
  }

  update(person: PersonV1): Observable<PersonV1> {
    return this.http.put<PersonV1>(`${this.base}`, person).pipe(catchError(handleApiError));
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }

  disable(id: string): Observable<PersonV1> {
    return this.http.patch<PersonV1>(`${this.base}/${id}`, null).pipe(catchError(handleApiError));
  }

  massCreate(file: File): Observable<PersonV1[]> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<PersonV1[]>(`${this.base}/massCreate`, form).pipe(catchError(handleApiError));
  }

  exportPage(p: PageParams, accept: string): Observable<Blob> {
    return this.http
      .get(`${this.base}/generateExportPage`, {
        params: this.pageParams(p),
        headers: { Accept: accept },
        responseType: 'blob',
      })
      .pipe(catchError(handleApiError));
  }
}
