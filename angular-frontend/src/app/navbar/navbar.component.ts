import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {TokenService} from '../service/token.service';
import {LocalUser} from '../models/local-user';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
    user: LocalUser = new LocalUser([]);
    isLogged: boolean = false;

    constructor(
        private router: Router,
        private tokenService: TokenService
    ) {}

    ngOnInit() {
        this.tokenService.isLogged().subscribe(data => {
            this.isLogged = data;
        })
        this.router.events.subscribe(
            (val: any) => {
                if (sessionStorage.getItem('AuthUser') && !val.url) {
                    this.updateInfo();
                }
            });
    }

    updateInfo() {
        this.user = this.tokenService.getUser();
    }
}
