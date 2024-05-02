import { Injectable } from '@angular/core';
import { Observable, catchError, of, tap } from 'rxjs';
import { School } from './school';
import { SCHOOLS } from './mock-schools';
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
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead
      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  upadateSchool(school: School): Observable<any> {
    return this.http.put(this.schoolsUrl, school, this.httpOptions).pipe(
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
      // if not search term, return empty hero array.
      return of([]);
    }
    return this.http.get<School[]>(`${this.schoolsUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
         this.log(`found schools matching "${term}"`) :
         this.log(`no schools matching "${term}"`)),
      catchError(this.handleError<School[]>('searchSchools', []))
    );
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  private schoolsUrl = 'api/schools';  // URL to web api

}
