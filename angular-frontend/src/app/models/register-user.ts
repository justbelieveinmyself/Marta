export class RegisterUser {
    firstName!: string;
    lastName!: string;
    username!: string;
    password!: string;
    passwordConfirm!: string;
    email!: string;
    constructor(
        firstName: string,
        lastName: string,
        username: string,
        password: string,
        passwordConfirm: string,
        email: string
        ){
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.password = password;
            this.passwordConfirm = passwordConfirm;
    }
}
