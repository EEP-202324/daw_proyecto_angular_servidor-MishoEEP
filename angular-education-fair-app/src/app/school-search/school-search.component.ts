import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';
import { School } from '../school';
import { SchoolService } from '../school.service';



@Component({
  selector: 'app-school-search',
  templateUrl: './school-search.component.html',
  styleUrls: [ './school-search.component.css' ]
})
export class SchoolSearchComponent implements OnInit {
  schools$!: Observable<School[]>;
  private searchTerms = new Subject<string>();

  constructor(private schoolService: SchoolService) {}

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.schools$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.schoolService.searchSchools(term)),
    );
  }
}
