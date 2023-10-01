import { Component } from '@angular/core';
import { TokenService } from '../service/token.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {
  constructor(
    private tokenService : TokenService
  ){}
  logOut(){
    this.tokenService.logOut();
  }
}
