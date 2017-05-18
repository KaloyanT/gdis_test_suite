import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  get watjevar(): string {
    return this._watjevar;
  }

  set watjevar(value: string) {
    this._watjevar = value;
  }

  constructor() { }

  ngOnInit() {
  }
  private _watjevar='';

}
