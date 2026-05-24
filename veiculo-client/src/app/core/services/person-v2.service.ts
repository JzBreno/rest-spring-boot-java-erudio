import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';
import { PageParams, PersonV2 } from '../../models/person.model';

@Injectable({ providedIn: 'root' })
export class PersonV2Service {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiBaseUrl}/person/v2`;

  private pageParams(p: PageParams): HttpParams {
    return new HttpParams()
      .set('page', String(p.page ?? 0))
      .set('size', String(p.size ?? 15))
      .set('direction', p.direction ?? 'asc')
      .set('sort', p.sort ?? p.properties ?? 'firstName');
  }

  findById(id: string): Observable<unknown> {
    return this.http.get(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }

  findAll(p: PageParams = {}): Observable<unknown> {
    return this.http
      .get(`${this.base}/findAll`, { params: this.pageParams(p), observe: 'response' })
      .pipe(
        map((r: HttpResponse<unknown>) => (r.status === 204 ? { content: [] } : r.body)),
        catchError(handleApiError),
      );
  }

  findByName(firstName: string, p: PageParams = {}): Observable<unknown> {
    return this.http
      .get(`${this.base}/findbyname/${encodeURIComponent(firstName)}`, {
        params: this.pageParams(p),
        observe: 'response',
      })
      .pipe(
        map((r: HttpResponse<unknown>) => (r.status === 204 ? { content: [] } : r.body)),
        catchError(handleApiError),
      );
  }

  create(person: PersonV2): Observable<unknown> {
    return this.http.post(`${this.base}`, person).pipe(catchError(handleApiError));
  }

  update(person: PersonV2): Observable<unknown> {
    return this.http.put(`${this.base}`, person).pipe(catchError(handleApiError));
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }

  massCreate(file: File): Observable<PersonV2[]> {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<PersonV2[]>(`${this.base}/massCreate`, form).pipe(catchError(handleApiError));
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
