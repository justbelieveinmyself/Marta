import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { FileHandle } from 'src/app/models/file-handle.model';
import { RegisterUser } from 'src/app/models/register-user';
import { AuthService } from 'src/app/service/auth.service';
import { TokenService } from 'src/app/service/token.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit{
  constructor(
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router,
    private sanitizer: DomSanitizer
  ){}

  isLogged! : boolean;
  isRegisterFail: boolean = false;

  firstName!: string;
  lastName!: string;
  username!: string;
  password!: string;
  passwordConfirm!: string;
  email!: string;
  avatar!: File;

  errorMessage!: string;

  regUser!: RegisterUser;
  ngOnInit(): void {
    if(this.tokenService.getToken()){
      this.isLogged = true;
      this.isRegisterFail = false;
    }
  }

  onRegister(){
    this.regUser = new RegisterUser(this.firstName, this.lastName, this.username, this.password, this.passwordConfirm, this.email);
    this.authService.register(this.regUser, this.avatar).subscribe({
      next: n => {
        this.isRegisterFail = false;
        this.router.navigate(["/login"])
      },
      error: e => {
        this.isRegisterFail = true;
        this.errorMessage = e.error.message;
      }
    });
  }
  onFileSelected(event : any){
    this.avatar = event.target.files[0];
  }
}
