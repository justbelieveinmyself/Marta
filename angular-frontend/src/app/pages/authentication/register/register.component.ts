import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {RegisterUser} from 'src/app/models/register-user';
import {AuthService} from 'src/app/services/auth.service';
import {TokenService} from 'src/app/services/token.service';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {moment} from "ngx-bootstrap/chronos/testing/chain";

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css', '../login/login.component.css']
})
export class RegisterComponent implements OnInit {
    constructor(
        private authService: AuthService,
        private tokenService: TokenService,
        private router: Router,
        private formBuilder: FormBuilder
    ) {}

    registerUser: RegisterUser = new RegisterUser();
    avatar: File;
    countries: string[] = ["Russian Federation", "Ukraine", "Kazakhstan", "Belarus"];
    isRegisterFail: boolean = false;
    errorMessage: string;
    isLogged: boolean;
    registerForm: FormGroup;

    ngOnInit(): void {
        if (this.tokenService.getRefreshToken()) {
            this.isLogged = true;
            this.isRegisterFail = false;
        }
        this.registerForm = this.formBuilder.group({
            firstName: ["", [Validators.required] ],
            lastName: ["", [Validators.required]],
            username: ["", [Validators.required]],
            email: ["", [Validators.required]],
            password: ["", [Validators.required]],
            passwordConfirm: ["", [Validators.required]],
            birthDate: ["", [Validators.required]],
            phone: ["", [Validators.required]],
            gender: ["", [Validators.required]],
            address: ["", [Validators.required]],
            address2: [""],
            country: ["", [Validators.required]],
            city: ["", [Validators.required]],
            region: ["", [Validators.required]],
            postalCode: ["", [Validators.required]]
        });

    }

    onRegister() {
        console.log(this.registerForm.get('firstName').hasError('required'))
        if(this.registerForm.invalid) {
            return;
        }

        this.registerUser.firstName = this.registerForm.value.firstName;
        this.registerUser.lastName = this.registerForm.value.lastName;
        this.registerUser.username = this.registerForm.value.username;
        this.registerUser.email = this.registerForm.value.email;
        this.registerUser.password = this.registerForm.value.password;
        this.registerUser.passwordConfirm = this.registerForm.value.passwordConfirm;
        this.registerUser.birthDate = this.registerForm.value.birthDate;
        this.registerUser.phone = this.registerForm.value.phone;
        this.registerUser.gender = this.registerForm.value.gender;
        this.registerUser.address = this.registerForm.value.address;
        this.registerUser.address2 = this.registerForm.value.address2;
        this.registerUser.country = this.registerForm.value.country;
        this.registerUser.city = this.registerForm.value.city;
        this.registerUser.region = this.registerForm.value.region;
        this.registerUser.postalCode = this.registerForm.value.postalCode;

        this.authService.register(this.registerUser, this.avatar).subscribe({
            next: n => {
                this.isRegisterFail = false;
                this.router.navigate(["/login"]);
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
