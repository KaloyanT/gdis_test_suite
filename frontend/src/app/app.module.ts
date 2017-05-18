import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { NavbarComponent } from './dashboard/navbar/navbar.component';
import {PasswordComponent } from './password/password.component';
import { FooterComponent } from './footer/footer.component';
import {GreetingsComponent } from './dashboard/greetings/greetings.component';
import { WarningAlertComponent } from './warning-alert/warning-alert.component';
import {UsernameComponent} from "./username/username.component";
import { SuccessAlertComponent } from './success-alert/succes-alert.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { StoryComponent } from './dashboard/story/story.component';
import { NewStoryComponent } from './dashboard/new-story/new-story.component';
import { StoryListComponent } from './dashboard/story/story-list/story-list.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    PasswordComponent,
    GreetingsComponent,
    WarningAlertComponent,
    UsernameComponent,
    SuccessAlertComponent,
    DashboardComponent,
    StoryComponent,
    NewStoryComponent,
    StoryListComponent

  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
