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
   page: number = 0;
   size: number = 3;
   totalSchools: number = 0;

  constructor(private schoolService: SchoolService, private messageService: MessageService) {}


  ngOnInit(): void {
    this.getSchools();
  }

  // getSchools(): void {
  //   this.schoolService.getSchools()
  //     .subscribe(schools => this.schools = schools);
  // }

  getSchools(): void {
    this.schoolService.getSchools(this.page, this.size)
    .subscribe(schools => this.schools = schools);
        // this.schools = data.content;
        // this.totalSchools = data.totalElements;});
  }


  add(name: string, city: string, rating: number): void {
    name = name.trim();
    city = city.trim();

    if (!name || !city || rating == null) {
      // alert('Please fill all the fields');
      return;
    }
    this.schoolService.addSchool({ name, city, rating } as School)
      .subscribe(responce => {
        this.getSchools();
        // alert('Added successfully!');
      });
  }

  delete(school: School): void {
    this.schools = this.schools.filter(s => s !== school);
    this.schoolService.deleteSchool(school.id).subscribe();
    // alert('School has been successfully deleted');
  }

  nextPage(): void {
    this.page++;
    this.getSchools();
  }

  previousPage(): void {
    this.page = Math.max(0, this.page - 1);
    this.getSchools();
  }
}


