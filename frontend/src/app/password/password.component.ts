import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.css']
})
export class PasswordComponent implements OnInit {
  get passwordIsCorrect(): boolean {
    return this._passwordIsCorrect;
  }

  set passwordIsCorrect(value: boolean) {
    this._passwordIsCorrect = value;
  }

  constructor() {
  }

  ngOnInit() {
  }

  password = '';
  private _passwordIsCorrect = false;

  passwordCorrect(){
    this.password.length > 4 ? this._passwordIsCorrect = true : this._passwordIsCorrect = false;
  }

}

