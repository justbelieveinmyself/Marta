import {
    BaseLoginProvider,
    GoogleLoginProvider,
    SocialAuthService,
    SocialUser,
    VKLoginProvider
} from '@abacritt/angularx-social-login';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from '../../service/user.service';
import {TokenService} from 'src/app/service/token.service';
import {AuthService} from 'src/app/service/auth.service';
import {LoginUser} from 'src/app/models/login-user';
import {RegisterUser} from 'src/app/models/register-user';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    constructor(
        private socialAuthService: SocialAuthService,
        private router: Router,
        private userService: UserService,
        private tokenService: TokenService,
        private authService: AuthService
    ) {
    }

    socialUser: SocialUser = new SocialUser;
    userLogged: SocialUser = new SocialUser;

    loginUser: LoginUser | undefined;
    isLogged: boolean = false;
    isLoginFail: boolean = false;
    username!: string;
    password!: string;
    errorMessage!: string;

    ngOnInit(): void {
        if (this.tokenService.getRefreshToken()) {
            this.isLogged = true;
            this.isLoginFail = false;
        }
    }

    onLogin(): void {
        this.loginUser = new LoginUser(this.username, this.password);
        this.authService.login(this.loginUser).then(
            n => {
                this.isLogged = true;
                this.isLoginFail = false;
                console.log("before", n.user)
                this.tokenService.setUser(n.user);
                this.authService.getAccessToken(n.refreshToken).subscribe({
                    next: n => {
                        this.tokenService.setRefreshToken(n.refreshToken);
                        this.tokenService.setAccessToken(n.accessToken);
                        this.router.navigate(['products']);
                    },
                    error: e => {
                        this.isLogged = false;
                        this.isLoginFail = true;
                        this.errorMessage = e.error.message;
                    }
                })

            }).catch(e => {
            this.isLogged = false;
            this.isLoginFail = true;
            this.errorMessage = e.error.message;
        })
    }

    signInWithVK(): void {
        this.socialAuthService.signIn(VKLoginProvider.PROVIDER_ID).then(
            data => {
                this.socialUser = data;
                this.isLogged = true;
                this.userService.getUserFromDbByOauth(data.id, data.authToken);
                this.router.navigate(['products']);
            });
    }
}
