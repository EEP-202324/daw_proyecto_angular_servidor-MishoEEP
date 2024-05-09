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

  getSchools(): Observable<School[]> {
    return this.http.get<School[]>(this.schoolsUrl)
      .pipe(
        tap(_ => this.log('fetched schools')),
        catchError(this.handleError<School[]>(`this.getSchools`, []))
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
      return of(result as T)
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      ;
    };
  }

  upadateSchool(school: School): Observable<any> {
    return this.http.put(`${this.schoolsUrl}/${school.id}`, school, this.httpOptions).pipe(
      tap(_ => this.log(`updated school id=${school.id}`)),
      catchError(this.handleError<any>('updateSchool'))
    );
  }
  addSchool(school: School): Observable<School> {
    return this.http.post<School>(this.schoolsUrl, school, this.httpOptions).pipe(
      tap((newSchool: School) => this.log(`added school w/ id=${newSchool.id}`)),
      catchError(this.handleError<School>('addSchool'))
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
    if (!term.trim()) {
      return of([]);
    }
    return this.http.get<School[]>(`${this.schoolsUrl}?name=${term}`).pipe(
      tap(x => x.length ?
         this.log(`found schools matching "${term}"`) :
         this.log(`no schools matching "${term}"`)),
      catchError(this.handleError<School[]>('searchSchools', []))
    );
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  private schoolsUrl = 'http://localhost:8080/schools';

}
