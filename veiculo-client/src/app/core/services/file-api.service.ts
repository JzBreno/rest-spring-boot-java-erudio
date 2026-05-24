import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';
import { UploadFileResponse } from '../../models/file.model';

@Injectable({ providedIn: 'root' })
export class FileApiService {
  private readonly http = inject(HttpClient);

  uploadFile(version: 'v1' | 'v2', file: File): Observable<UploadFileResponse> {
    const form = new FormData();
    form.append('file', file);
    return this.http
      .post<UploadFileResponse>(`${environment.apiBaseUrl}/api/files/${version}/uploadFile`, form)
      .pipe(catchError(handleApiError));
  }

  uploadFiles(version: 'v1' | 'v2', files: File[]): Observable<UploadFileResponse[]> {
    const form = new FormData();
    files.forEach((f) => form.append('files', f));
    return this.http
      .post<UploadFileResponse[]>(`${environment.apiBaseUrl}/api/files/${version}/uploadFiles`, form)
      .pipe(catchError(handleApiError));
  }

  download(version: 'v1' | 'v2', fileName: string): Observable<Blob> {
    return this.http
      .get(`${environment.apiBaseUrl}/api/files/${version}/downloadfile/${encodeURIComponent(fileName)}`, {
        responseType: 'blob',
      })
      .pipe(catchError(handleApiError));
  }
}
