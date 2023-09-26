import { SocialAuthService, SocialUser } from '@abacritt/angularx-social-login';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Marta';
  userLogged: SocialUser = new SocialUser;
  isLogged: boolean = false;
  constructor(private router : Router, private authService : SocialAuthService){}
  ngOnInit(){
    this.authService.authState.subscribe(
      data => {
        this.userLogged = data;
        this.isLogged = (this.userLogged != null);
      }
    )
  }
  logOut(){
    this.authService.signOut().then(
      data => {
        this.router.navigate(['login']);
      }
    );
  }
}
