import {Component} from '@angular/core';
import {TokenService} from '../../../services/token.service';
import {LocalUser} from '../../../models/local-user';
import {UserService} from "../../../services/user.service";

@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent {
    user: LocalUser;

    constructor(
        private tokenService: TokenService,
        private userService: UserService
    ) {}

    ngOnInit() {
        this.user = this.tokenService.getUser();
        if (this.user != null) {
            this.userService.getUserAvatar(this.user.id).then(
                res => {
                    this.user.avatar = res;
                }
            ).catch(error => this.user.avatar = "https://eliaslealblog.files.wordpress.com/2014/03/user-200.png")
        }
    }
}
