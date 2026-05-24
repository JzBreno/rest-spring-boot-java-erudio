import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { handleApiError } from '../http/api-error.util';
import { Book } from '../../models/book.model';

@Injectable({ providedIn: 'root' })
export class BookService {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiBaseUrl}/book`;

  findById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }

  findAll(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.base}/findAll`).pipe(catchError(handleApiError));
  }

  create(book: Book): Observable<Book> {
    return this.http.post<Book>(`${this.base}/create`, book).pipe(catchError(handleApiError));
  }

  save(book: Book): Observable<Book> {
    return this.http.post<Book>(`${this.base}`, book).pipe(catchError(handleApiError));
  }

  update(book: Book): Observable<Book> {
    return this.http.put<Book>(`${this.base}/update`, book).pipe(catchError(handleApiError));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`).pipe(catchError(handleApiError));
  }
}
