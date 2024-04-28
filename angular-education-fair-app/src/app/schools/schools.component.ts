import { Component, OnInit } from '@angular/core';
import { School } from '../school';

import { SchoolService } from '../school.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-schools',
  templateUrl: './schools.component.html',
  styleUrl: './schools.component.css'
})

export class SchoolsComponent implements OnInit{
  selectedSchool?: School;
  schools: School[] = [];

  constructor(private schoolService: SchoolService, private messageService: MessageService) {}


ngOnInit(): void {
  this.getSchools();
}

onSelect(school: School): void {
  this.selectedSchool = school;
  this.messageService.add(`SchoolsComponent: Selected school id=${school.id}`);
}

getSchools(): void {
  this.schoolService.getSchools()
    .subscribe(schools => this.schools = schools);
}



}
