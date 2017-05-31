import { Component, Input } from '@angular/core';
import {$$} from "@angular/compiler/src/chars";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Log In';
  password = '';
  @Input() username='';
  buttonClicked = false;
  switchToDashboard=false;

  onPasswordUpdate(event: Event){
    this.password = (<HTMLInputElement>event.target).value;
  }

  onUsernameUpdate(event: Event){
    this.username = (<HTMLInputElement>event.target).value;
}

  inputCorrect(){
    return (this.password.length > 4 && this.username.length > 0) ? true : false;
  }

  onClickButon(){
    this.buttonClicked = true;
  }

  onClickDashboard(){
    this.switchToDashboard = true;
  }
}
