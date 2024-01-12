import {Observable} from "rxjs";
import {Router, UrlTree} from "@angular/router";
import {TokenService} from "./services/token.service";
import {Injectable} from "@angular/core";

@Injectable({
    providedIn: 'root'
})
export class AdminGuard {

    constructor(
        private router: Router,
        private tokenService: TokenService
    ) {}

    canActivate(): | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (this.tokenService.getUser().roles.includes("ADMIN")) {
            return true;
        }
        this.router.navigate(['profile']);
        return false;
    }
}
