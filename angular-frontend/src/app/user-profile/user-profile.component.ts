import { Component, SecurityContext } from '@angular/core';
import { TokenService } from '../service/token.service';
import { LocalUser } from '../models/local-user';
import { UserService } from '../service/user.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ImageService } from '../service/image.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {
  user !:LocalUser;
  constructor(
    private tokenService : TokenService,
    private imageService : ImageService
  ){}
  logOut(){
    this.tokenService.logOut();
  }
  ngOnInit(){
    this.user = this.tokenService.getUser();
    if(this.user != null){
      this.imageService.getUserAvatar(this.user.id).then(
        res => {
            this.user.avatar = res;
            console.log(res)
        }
    ).catch(error => this.user.avatar = "https://eliaslealblog.files.wordpress.com/2014/03/user-200.png") 
    }
  }
}
