import { Component, OnInit } from '@angular/core';
import { TokenService } from '../service/token.service';
import { LocalUser } from '../models/local-user';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['../user-profile/user-profile.component.css']
})
export class UserDetailsComponent implements OnInit {
  user!: LocalUser;
  newemail!: string;
  constructor(
    private tokenService: TokenService
  ){}
  ngOnInit(){
    this.user = this.tokenService.getUser();
  }
}
