import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SchoolsComponent } from './schools/schools.component';
import { FormsModule } from '@angular/forms';
import { SchoolDetailComponent } from './school-detail/school-detail.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HttpClientModule } from '@angular/common/http';
import { SchoolSearchComponent } from './school-search/school-search.component';

@NgModule({
  declarations: [
    AppComponent,
    SchoolsComponent,
    SchoolDetailComponent,
    DashboardComponent,
    SchoolSearchComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    // HttpClientInMemoryWebApiModule.forRoot(
    //   InMemoryDataService, { dataEncapsulation: false }
    // ),
  ],
  providers: [
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
