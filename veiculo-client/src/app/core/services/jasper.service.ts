import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';
import { PageParams } from '../../models/person.model';

@Injectable({ providedIn: 'root' })
export class JasperService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiBaseUrl}/person/v2/reports/jasper`;

  private params(p: PageParams): HttpParams {
    return new HttpParams()
      .set('page', String(p.page ?? 0))
      .set('size', String(p.size ?? 15))
      .set('direction', p.direction ?? 'asc')
      .set('sort', p.sort ?? 'firstName');
  }

  exportPdf(p: PageParams = {}): Observable<Blob> {
    return this.http
      .get(`${this.base}/people/pdf`, { params: this.params(p), responseType: 'blob' })
      .pipe(catchError(handleApiError));
  }

  exportXlsx(p: PageParams = {}): Observable<Blob> {
    return this.http
      .get(`${this.base}/people/xlsx`, { params: this.params(p), responseType: 'blob' })
      .pipe(catchError(handleApiError));
  }
}
