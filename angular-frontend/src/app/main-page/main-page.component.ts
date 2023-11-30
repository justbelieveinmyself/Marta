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
    sizeOfPage: number = 12;
    sortBy: string;
    page: Page<ProductWithImage>;
    countOfVerifiedProduct = 0;
    countOfProductWithImage = 0;
    isFilteredByVerified = false;
    isFilteredByWithPhoto = false;
    isSortASC = false;
    isNeedSortByDate = false;
    isNeedSortByPrice = false;
    ngOnInit(): void {
        this.user = this.tokenService.getUser();
        if(!this.user){
            this.errorIntercept.updateAccess();
            return;
        }
        this.getProducts(0);
    }

    getProducts(page: number) {
        this.productService.getProductList(page, this.sizeOfPage, true, this.sortBy, this.isSortASC).subscribe({
            next: data => {
                if (this.isFilteredByVerified) {
                    this.products = data.content.filter(product => product.product.isVerified);
                } else if(this.isFilteredByWithPhoto) {
                    this.products = data.content.filter(product => product.file.length > 150);
                } else {
                    this.products = data.content;
                }
                this.copyOfProducts = data.content;
                this.page = data;
                this.countOfVerifiedProduct = data.content.filter(product => product.product.isVerified).length;
                this.countOfProductWithImage = data.content.filter(product => product.file.length > 150).length;
            }
        });
    }

    filterByVerified(event: any) {
        this.isFilteredByVerified = event.target.checked;
        if (this.isFilteredByVerified) {
            this.products = this.products.filter(product => product.product.isVerified);
        } else if(this.isFilteredByWithPhoto) {
            this.products = this.copyOfProducts.filter(product => product.file.startsWith("blob"));
        } else {
            this.products = this.copyOfProducts;
        }
    }

    protected readonly Array = Array;

    filterByWithPhoto(event: any) {
        this.isFilteredByWithPhoto = event.target.checked;
        if (this.isFilteredByWithPhoto) {
            this.products = this.products.filter(product => product.file.startsWith("blob"));
        } else if(this.isFilteredByVerified) {
            this.products = this.copyOfProducts.filter(product => product.product.isVerified);
        } else {
            this.products = this.copyOfProducts;
        }
    }



    sortByDate() {
        this.sortBy = "updatedAt";
        this.isSortASC = !this.isSortASC;
        this.getProducts(0);
        this.isNeedSortByPrice = false;
        this.isNeedSortByDate = true;

    }

    sortByPrice() {
        this.sortBy = "price";
        this.isSortASC = !this.isSortASC;
        this.getProducts(0);
        this.isNeedSortByDate = false;
        this.isNeedSortByPrice = true;
    }

}
