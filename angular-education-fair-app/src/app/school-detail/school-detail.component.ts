import {Component, Input} from '@angular/core';
import { School } from '../school';

import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { SchoolService } from '../school.service';


@Component({
  selector: 'app-school-detail',
  templateUrl: './school-detail.component.html',
  styleUrl: './school-detail.component.css'
})
export class SchoolDetailComponent {
  @Input() school?: School;

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
}
