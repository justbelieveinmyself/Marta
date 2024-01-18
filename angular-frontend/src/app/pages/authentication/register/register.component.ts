import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {RegisterUser} from 'src/app/models/register-user';
import {AuthService} from 'src/app/services/auth.service';
import {TokenService} from 'src/app/services/token.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
    constructor(
        private authService: AuthService,
        private tokenService: TokenService,
        private router: Router
    ) {}

    isLogged!: boolean;
    isRegisterFail: boolean = false;

    firstName!: string;
    lastName!: string;
    username!: string;
    password!: string;
    passwordConfirm!: string;
    email!: string;
    avatar!: File;

    errorMessage!: string;

    ngOnInit(): void {
        if (this.tokenService.getRefreshToken()) {
            this.isLogged = true;
            this.isRegisterFail = false;
        }
    }

    onRegister() {
        const regUser = new RegisterUser(this.firstName, this.lastName, this.username, this.password, this.passwordConfirm, this.email);
        this.authService.register(regUser, this.avatar).subscribe({
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

    onFileSelected(event: any) {
        this.avatar = event.target.files[0];
    }
}
