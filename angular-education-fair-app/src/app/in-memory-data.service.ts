import { Injectable } from '@angular/core';
import { InMemoryDbService } from 'angular-in-memory-web-api';
import { School } from './school';

@Injectable({
  providedIn: 'root',
})
export class InMemoryDataService implements InMemoryDbService {
  createDb() {
    const schools = [
      { id: 12, name: 'EEP Igroup', city: 'Madrid', rating: '9' },
      { id: 13, name: 'U-tad' , city: 'Madrid', rating: '10'},
      { id: 14, name: 'Trazos', city: 'Madrid', rating: '8' },
      { id: 15, name: 'LightboxAcademy' , city: 'Madrid', rating: '9'},
      { id: 16, name: 'ESNE', city: 'Madrid', rating: '9' },
      { id: 17, name: 'CESUR', city: 'Madrid', rating: '7' },
      { id: 18, name: 'CEAC', city: 'Madrid', rating: '8' },
      { id: 19, name: 'Universidad Complutense', city: 'Madrid', rating: '9' },
      { id: 20, name: 'ISEP CEU', city: 'Madrid', rating: '7' }
    ];
    return {schools};
  }

  genId(schools: School[]): number {
    return schools.length > 0 ? Math.max(...schools.map(school => school.id)) + 1 : 11;
  }
}
