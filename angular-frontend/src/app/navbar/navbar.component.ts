import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../service/token.service';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  username!: string;
  isLogged: boolean = false;
  constructor(private router : Router, private tokenService: TokenService){}
  ngOnInit(){
    this.tokenService.isLogged().subscribe(data => {
      this.isLogged = data;
    })
  }
  logOut(){
    this.tokenService.logOut();
    this.router.navigate(['/login']);
  }
}
