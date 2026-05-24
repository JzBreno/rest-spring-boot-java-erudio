import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

export interface ApiErrorBody {
  timestamp?: string;
  message?: string;
  details?: string;
}

export function handleApiError(error: HttpErrorResponse): Observable<never> {
  const body = error.error as ApiErrorBody | string | null;
  const message =
    typeof body === 'string'
      ? body
      : body?.message ?? error.message ?? 'Erro ao comunicar com a API';
  return throwError(() => new Error(message));
}
