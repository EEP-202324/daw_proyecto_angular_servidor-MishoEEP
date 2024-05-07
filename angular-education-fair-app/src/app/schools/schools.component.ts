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

   schools: School[] = [];

  constructor(private schoolService: SchoolService, private messageService: MessageService) {}


  ngOnInit(): void {
    this.getSchools();
  }

  getSchools(): void {
    this.schoolService.getSchools()
      .subscribe(schools => this.schools = schools);
  }

  // add(name: string): void {
  //   name = name.trim();
  //   if (!name) { return; }
  //   this.schoolService.addSchool({ name } as School)
  //     .subscribe(school => {
  //       this.schools.push(school);
  //     });
  // }


  add(name: string, city: string, rating: number): void {
    // Trim the input values to ensure clean data entry
    name = name.trim();
    city = city.trim();

    if (!name || !city || rating == null) { return; }
    // if (!name || !city || !rating) { return; }

    this.schoolService.addSchool({ name, city, rating } as School)
      .subscribe(school => {
        this.schools.push(school);
      });
}


  delete(school: School): void {
    this.schools = this.schools.filter(s => s !== school);
    this.schoolService.deleteSchool(school.id).subscribe();
  }
}
