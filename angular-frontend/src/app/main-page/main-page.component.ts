import {Component} from '@angular/core';
import {TokenService} from "../service/token.service";
import {LocalUser} from "../models/local-user";
import {ProductService} from "../service/product.service";
import {ProductWithImage} from "../models/product-with-image";
import {ErrorInterceptService} from "../service/error-intercept.service";
import {Page} from "../models/page";

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
    page: Page;
    ngOnInit(): void {
        this.user = this.tokenService.getUser();
        if(!this.user){
            this.errorIntercept.updateAccess();
            return;
        }
        this.getProducts(this.currentPage);
    }

    getProducts(page: number) {
        this.productService.getProductList(page, this.sizeOfPage, true).subscribe({
            next: data => {
                this.products = data.content;
                this.page = data;
                this.currentPage = data.pageable.pageNumber;
            }
        });
    }

    protected readonly Array = Array;
}
