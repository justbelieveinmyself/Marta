import {Component} from '@angular/core';
import {TokenService} from "../service/token.service";
import {LocalUser} from "../models/local-user";
import {ProductService} from "../service/product.service";
import {ProductWithImage} from "../models/product-with-image";
import {ErrorInterceptService} from "../service/error-intercept.service";
import {Page} from "../models/page";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {map} from "rxjs";
import {PageDataService} from "../service/page-data.service";

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
        private activatedRoute: ActivatedRoute,
        public pageDataService: PageDataService
    ) {}

    user: LocalUser;
    products: ProductWithImage[];
    page: Page<ProductWithImage>;
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
                if(param != this.pageDataService.searchWord) {
                    this.pageDataService.searchWord = param;
                    this.getProducts(0);
                }
            }
        });
        this.activatedRoute.data.subscribe(data => {
            this.page = data['productsPage'];
            this.products = this.page.content;
        });
    }

    getProducts(page?: number) {
        if(page != null) {
            this.pageDataService.pageNumber = page;
        }
        this.productService.getProductList(this.pageDataService.pageNumber, this.pageDataService.sizeOfPage, true, this.pageDataService.sortBy, this.pageDataService.isSortASC, this.pageDataService.isFilteredByWithPhoto, this.pageDataService.isFilteredByVerified, this.pageDataService.searchWord).subscribe({
            next: data => {
                this.products = data.content;
                this.page = data;
            }
        });
    }

    protected readonly Array = Array;

    sortByDate() {
        this.pageDataService.sortBy = "updatedAt";
        this.pageDataService.isSortASC = !this.pageDataService.isSortASC;
        this.getProducts(0);
        this.isNeedSortByPrice = false;
        this.isNeedSortByDate = true;
    }

    sortByPrice() {
        this.pageDataService.sortBy = "price";
        this.pageDataService.isSortASC = !this.pageDataService.isSortASC;
        this.getProducts(0);
        this.isNeedSortByDate = false;
        this.isNeedSortByPrice = true;
    }

}
