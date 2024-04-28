import { Component } from '@angular/core';
import { School } from '../school';
import { SCHOOLS } from '../mock-schools';

@Component({
  selector: 'app-schools',
  templateUrl: './schools.component.html',
  styleUrl: './schools.component.css'
})

export class SchoolsComponent {
  schools = SCHOOLS;

  selectedSchool?: School;
onSelect(school: School): void {
  this.selectedSchool = school;
}
}
