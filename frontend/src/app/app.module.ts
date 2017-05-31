import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import {GreetingsComponent } from './dashboard/greetings/greetings.component';
import { WarningAlertComponent } from './warning-alert/warning-alert.component';
import { SuccessAlertComponent } from './success-alert/succes-alert.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { StoryComponent } from './dashboard/story/story.component';
import { NewStoryComponent } from './dashboard/new-story/new-story.component';
import { StoryListComponent } from './dashboard/story/story-list/story-list.component';
import {Routes, RouterModule} from "@angular/router";

const appRoutes: Routes = [
  {path: 'dashboard', component: DashboardComponent},
]
@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    GreetingsComponent,
    WarningAlertComponent,
    SuccessAlertComponent,
    DashboardComponent,
    StoryComponent,
    NewStoryComponent,
    StoryListComponent

  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
