import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-username',
  templateUrl: './username.component.html',
  styleUrls: ['./username.component.css']
})
export class UsernameComponent implements OnInit {
  get usrFilled(): boolean {
    return this._usrFilled;
  }

  set usrFilled(value: boolean) {
    this._usrFilled = value;
  }

  constructor() { }

  ngOnInit() {
  }

  username='';
  private _usrFilled = false;

  usernameFilled(){
    this.username.length > 0 ? this._usrFilled = true : this.usrFilled = false;
  }
}

