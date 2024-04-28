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

}
