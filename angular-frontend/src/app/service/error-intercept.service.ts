import {
    HTTP_INTERCEPTORS,
    HttpErrorResponse,
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest
} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {ProductService} from "./product.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TokenService} from "./token.service";
import {AuthService} from "./auth.service";
import {UserService} from "./user.service";

@Injectable({
    providedIn: 'root'
})
export class ErrorInterceptService implements HttpInterceptor {
    constructor(
        private router: Router,
        private tokenService: TokenService,
        private authService: AuthService,
        private userService: UserService) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError((error: any) => {
                if (error instanceof HttpErrorResponse) {
                    if (error.error === "Bad access token!") {
                        this.updateAccess();
                    }
                }
                return throwError(error);
            })
        );
    }

    updateAccess() {
        if (this.tokenService.getRefreshToken() != null) {
            this.authService.getAccessToken(this.tokenService.getRefreshToken()).subscribe({
                next: token => {
                    this.tokenService.setAccessToken(token.accessToken);
                    this.tokenService.setRefreshToken(token.refreshToken);
                    this.userService.getUser().subscribe({
                        next: user => {
                            this.tokenService.setUser(user);
                        }
                    });
                    window.location.reload();

                },
                error: error => {
                    this.tokenService.logOut()
                    this.router.navigate(['/login']);
                }
            })
        }
    }
}

export const responseInterceptorProvider = [{
    provide: HTTP_INTERCEPTORS,
    useClass: ErrorInterceptService,
    multi: true
}]
