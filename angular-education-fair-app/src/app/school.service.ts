import { Injectable } from '@angular/core';
import { Observable, catchError, of, tap } from 'rxjs';
import { School } from './school';

import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SchoolService {
  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

  getSchools(page: number = 0, size: number = 5): Observable<any> {
    const url = `${this.schoolsUrl}?page=${page}&size=${size}`;
    return this.http.get<any>(url)
      .pipe(
        tap(_ => this.log('fetched schools')),
        catchError(this.handleError<any>('getSchools', {content: [], totalElements: 0}))
      );
  }

  getSchool(id: number): Observable<School> {
    const url = `${this.schoolsUrl}/${id}`;
    return this.http.get<School>(url).pipe(
      tap(_ => this.log(`fetched school id=${id}`)),
      catchError(this.handleError<School>(`getSchool id=${id}`))
    );
  }

  private log(message: string) {
    this.messageService.add(`SchoolService: ${message}`);
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  upadateSchool(school: School): Observable<any> {
    return this.http.put(`${this.schoolsUrl}/${school.id}`, school, this.httpOptions).pipe(
      tap(_ => this.log(`updated school id=${school.id}`)),
      catchError(this.handleError<any>('updateSchool'))
    );
  }
  addSchool(school: School): Observable<any> {
    return this.http.post<School>(this.schoolsUrl, school, this.httpOptions).pipe(
      tap(_ => this.log(`added school w/ id=${school.id}`)),
      catchError(this.handleError<any>('addSchool'))
    );
  }

  deleteSchool(id: number): Observable<School> {
    const url = `${this.schoolsUrl}/${id}`;

    return this.http.delete<School>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted school id=${id}`)),
      catchError(this.handleError<School>('deleteSchool'))
    );
  }

  searchSchools(term: string): Observable<School[]> {
    console.log('Starting the search', term);
    if (!term.trim()) {
      console.log('searchSchools: term is empty');
      return of([]);
    }
    const url = `${this.schoolsUrl}/search?name=${term}`;
    console.log(`searchSchools: URL = ${url}`);
    return this.http.get<School[]>(url).pipe(
      tap(x => {
        console.log(`searchSchools: response = `, x);
        if (x.length) {
          this.log(`found schools matching "${term}"`);
        } else {
          this.log(`no schools matching "${term}"`);
        }
      }),
      catchError(this.handleError<School[]>('searchSchools', []))
    );
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  private schoolsUrl = 'http://localhost:8080/schools';

}
