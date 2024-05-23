import {Component, Input} from '@angular/core';
import { School } from '../school';

import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { SchoolService } from '../school.service';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-school-detail',
  templateUrl: './school-detail.component.html',
  styleUrl: './school-detail.component.css'
})
export class SchoolDetailComponent {
  @Input() school?: School;
  showDeleteMessage: boolean = false;
  showSavedMessage: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private schoolService: SchoolService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getSchool();
  }

  getSchool(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.schoolService.getSchool(id)
      .subscribe(school => this.school = school);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.school) {
      this.schoolService.upadateSchool(this.school)
        .subscribe(() => this.showSuccessMessage('save'));
    }
  }
  deleteSchool(): void {
    if (this.school) {
      this.schoolService.deleteSchool(this.school.id).subscribe(() => {
        this.showSuccessMessage('delete');
      });
    }
  }

  showSuccessMessage(action: string): void {
    if (action === 'save') {
      this.showSavedMessage = true;
      setTimeout(() => {
        this.showSavedMessage = false;
        this.goBack();
      }, 3000); // Display the message for 3 seconds
    } else if (action === 'delete') {
      this.showDeleteMessage = true;
      setTimeout(() => {
        this.showDeleteMessage = false;
        this.goBack();
      }, 2000); // Display the message for 2 seconds
    }
  }
}
