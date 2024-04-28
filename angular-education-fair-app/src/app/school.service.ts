import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { School } from './school';
import { SCHOOLS } from './mock-schools';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class SchoolService {
  constructor(private messageService: MessageService) { }

  getSchools(): Observable<School[]> {
    const schools = of(SCHOOLS);
    this.messageService.add('SchoolService: fetched schools');
    return schools;
  }

  getSchool(id: number): Observable<School> {
    // For now, assume that a hero with the specified `id` always exists.
    // Error handling will be added in the next step of the tutorial.
    const school = SCHOOLS.find(h => h.id === id)!;
    this.messageService.add(`SchoolService: fetched school id=${id}`);
    return of(school);
  }

}
