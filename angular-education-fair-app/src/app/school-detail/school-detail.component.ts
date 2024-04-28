import {Component, Input} from '@angular/core';
import { School } from '../school';


@Component({
  selector: 'app-school-detail',
  templateUrl: './school-detail.component.html',
  styleUrl: './school-detail.component.css'
})
export class SchoolDetailComponent {
  @Input() school?: School;
}
