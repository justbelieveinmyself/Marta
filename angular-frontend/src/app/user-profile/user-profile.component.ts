import { Component } from '@angular/core';
import { TokenService } from '../service/token.service';
import { LocalUser } from '../models/local-user';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {
  user !:LocalUser;
  constructor(
    private tokenService : TokenService
  ){}
  logOut(){
    this.tokenService.logOut();
  }
  ngOnInit(){
    this.user = this.tokenService.getUser();
  }
}
