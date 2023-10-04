import { Component } from '@angular/core';
import { TokenService } from '../service/token.service';
import { LocalUser } from '../models/local-user';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {
  user !:LocalUser;
  path!:String;
  constructor(
    private tokenService : TokenService,
    private userService : UserService
  ){}
  logOut(){
    this.tokenService.logOut();
  }
  ngOnInit(){
    this.user = this.tokenService.getUser();
    if(this.user != null && this.path == null){
      this.userService.getAvatar(this.user.id).subscribe(res => {
        var url= window.URL.createObjectURL(res);
        this.path = url;
      });
    }
  }
}
