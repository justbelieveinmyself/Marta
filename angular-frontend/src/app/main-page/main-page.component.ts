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
        private tokenService: TokenService,
    ){}

    user!: LocalUser;
    products: ProductWithImage[];

    ngOnInit(): void {
        this.user = this.tokenService.getUser();
        this.getProducts();

    }

    private getProducts() {
        this.productService.getProductList().subscribe({
            next: data => {
                this.products = data;
            }
        });
    }

}
