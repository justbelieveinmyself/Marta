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
    copyOfProducts: ProductWithImage[];
    currentPage: number = 0;
    sizeOfPage: number = 12;
    page: Page;
    countOfVerifiedProduct = 0;
    countOfProductWithImage = 0;
    isFiltredByVerified = false;
    isFiltredByWithPhoto = false;
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
                this.copyOfProducts = data.content;
                this.page = data;
                this.countOfVerifiedProduct = data.content.filter(product => product.product.isVerified).length;
                this.countOfProductWithImage = data.content.filter(product => product.file.length > 150).length;
                this.currentPage = data.pageable.pageNumber;
            }
        });
    }

    filterByVerified(event: any) {
        this.isFiltredByVerified = event.target.checked;
        if (this.isFiltredByVerified) {
            this.products = this.products.filter(product => product.product.isVerified);
        } else if(this.isFiltredByWithPhoto) {
            this.products = this.copyOfProducts.filter(product => product.file.startsWith("blob"));
        } else {
            this.products = this.copyOfProducts;
        }
    }

    protected readonly Array = Array;

    filterByWithPhoto(event: any) {
        this.isFiltredByWithPhoto = event.target.checked;
        if (this.isFiltredByWithPhoto) {
            this.products = this.products.filter(product => product.file.startsWith("blob"));
        } else if(this.isFiltredByVerified) {
            this.products = this.copyOfProducts.filter(product => product.product.isVerified);
        } else {
            this.products = this.copyOfProducts;
        }
    }
}
