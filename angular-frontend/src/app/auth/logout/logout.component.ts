import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {TokenService} from 'src/app/service/token.service';

@Component({
    selector: 'app-logout',
    templateUrl: './logout.component.html',
    styleUrls: ['./logout.component.scss']
})
export class LogoutComponent {
    constructor(
        private router: Router,
        private tokenService: TokenService
    ) {
    }

    logOut() {
        this.tokenService.logOut();
        this.router.navigate(['/login']);
    }
}
