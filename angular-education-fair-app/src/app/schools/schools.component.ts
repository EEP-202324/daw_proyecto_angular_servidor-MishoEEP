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
  showSuccessMessage: boolean = false;
   page: number = 0;
   size: number = 4;
   totalSchools: number = 0;

  constructor(private schoolService: SchoolService, private messageService: MessageService) {}


  ngOnInit(): void {
    this.getSchools();
  }

  getSchools(): void {
    this.schoolService.getSchools(this.page, this.size)
    .subscribe(schools => this.schools = schools);
  }

  addSchoolWithMessage(name: string, city: string, rating: number): void {
    this.schoolService.addSchool({ name, city, rating } as School)
      .subscribe(responce => {
        this.getSchools();
        this.showSuccessMessage = true;
        setTimeout(() => {
          this.showSuccessMessage = false;
        }, 3000); // Hide the message after 3 seconds
      });
  }


  delete(school: School): void {
    this.schools = this.schools.filter(s => s !== school);
    this.schoolService.deleteSchool(school.id).subscribe();
  }

  nextPage(): void {
    this.page++;
    this.getSchools();
  }

  previousPage(): void {
    if (this.page > 0) {
      this.page--;
      this.getSchools();
    }
  }
}
