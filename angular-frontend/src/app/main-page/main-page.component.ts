import {Component} from '@angular/core';
import {TokenService} from "../service/token.service";
import {AuthService} from "../service/auth.service";
import {UserService} from "../service/user.service";
import {LocalUser} from "../models/local-user";
import {ProductService} from "../service/product.service";
import {Router} from "@angular/router";
import {ProductWithImage} from "../models/product-with-image";

@Component({
    selector: 'app-main-page',
    templateUrl: './main-page.component.html',
    styleUrls: ['./main-page.component.css']
})
export class MainPageComponent {
    constructor(
        private productService: ProductService,
        private router: Router,
        private tokenService: TokenService,
        private authService: AuthService,
        private userService: UserService) {
    }
    user!: LocalUser;
    products: ProductWithImage[];
    ngOnInit(): void {
        try {
            this.user = this.tokenService.getUser();
            this.getProducts();
        } catch {
            this.updateAccess();
        }

    }

    private getProducts() {
        this.productService.getProductList().subscribe({
            next: data => {
                this.products = data;
            },
            error: e => {
                console.log("1");
                this.updateAccess();
            }
        });
    }

    updateAccess() {
        if (this.tokenService.getRefreshToken() != null) {
            this.authService.getAccessToken(this.tokenService.getRefreshToken()).subscribe({
                next: token => {
                    this.tokenService.setAccessToken(token.accessToken);
                    this.tokenService.setRefreshToken(token.refreshToken);
                    this.getProducts();
                    this.userService.getUser().subscribe({
                        next: user => {
                            this.tokenService.setUser(user);
                            this.user = user;
                        }
                    });
                },
                error: error => {
                    console.log("error")
                    this.tokenService.logOut()
                    this.router.navigate(['/login']);
                }
            })
        }
    }
}
