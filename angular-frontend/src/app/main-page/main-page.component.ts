import {Component} from '@angular/core';
import {TokenService} from "../service/token.service";
import {LocalUser} from "../models/local-user";
import {ProductService} from "../service/product.service";
import {ProductWithImage} from "../models/product-with-image";
import {ErrorInterceptService} from "../service/error-intercept.service";
import {Page} from "../models/page";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {map} from "rxjs";

@Component({
    selector: 'app-main-page',
    templateUrl: './main-page.component.html',
    styleUrls: ['./main-page.component.css']
})
export class MainPageComponent {
    constructor(
        private productService: ProductService,
        private tokenService: TokenService,
        private errorIntercept: ErrorInterceptService,
        private activatedRoute: ActivatedRoute
    ) {}

    searchWord: string;
    user: LocalUser;
    products: ProductWithImage[];
    sizeOfPage: number = 12;
    sortBy: string;
    page: Page<ProductWithImage>;
    isFilteredByVerified = true;
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
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get('search'))).subscribe({
            next: param => {
                this.searchWord = param;
                this.getProducts(0);
            }
        });
        this.activatedRoute.data.subscribe(data => {
            this.page = data['productsPage'];
            this.products = this.page.content;
        });

    }

    getProducts(page: number) {
        this.productService.getProductList(page, this.sizeOfPage, true, this.sortBy, this.isSortASC, this.isFilteredByWithPhoto, this.isFilteredByVerified, this.searchWord).subscribe({
            next: data => {
                this.products = data.content;
                this.page = data;
            }
        });
    }

    protected readonly Array = Array;

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
