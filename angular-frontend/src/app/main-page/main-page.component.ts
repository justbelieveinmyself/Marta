import {Component} from '@angular/core';
import {TokenService} from "../service/token.service";
import {AuthService} from "../service/auth.service";
import {UserService} from "../service/user.service";
import {LocalUser} from "../models/local-user";
import {ProductService} from "../service/product.service";
import {Router} from "@angular/router";
import {ProductWithImage} from "../models/product-with-image";
import {ErrorInterceptService} from "../service/error-intercept.service";

@Component({
    selector: 'app-main-page',
    templateUrl: './main-page.component.html',
    styleUrls: ['./main-page.component.css']
})
export class MainPageComponent {
    constructor(
        private productService: ProductService,
        private tokenService: TokenService,
        private errorIntercept: ErrorInterceptService
    ) {}

    user!: LocalUser;
    products: ProductWithImage[];
    currentPage: number = 0;
    sizeOfPage: number = 12;

    ngOnInit(): void {
        this.user = this.tokenService.getUser();
        if(!this.user){
            this.errorIntercept.updateAccess();
        }
        this.getProducts();
    }

    private getProducts() {
        this.productService.getProductList(this.currentPage, this.sizeOfPage).subscribe({
            next: data => {
                this.products = data;
            }
        });
    }

}
