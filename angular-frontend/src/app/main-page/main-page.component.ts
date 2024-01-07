import {Component} from '@angular/core';
import {TokenService} from "../service/token.service";
import {LocalUser} from "../models/local-user";
import {ProductService} from "../service/product.service";
import {ProductWithImage} from "../models/product-with-image";
import {ErrorInterceptService} from "../service/error-intercept.service";
import {Page} from "../models/page";
import {ActivatedRoute, ActivatedRouteSnapshot, ParamMap, Router, UrlSerializer, UrlTree} from "@angular/router";
import {map} from "rxjs";
import {isNumber} from "ngx-bootstrap/carousel/utils";
import {isBoolean} from "ngx-bootstrap/chronos/utils/type-checks";
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
        private pageDataService: PageDataService
    ) {}

    searchWord: string = "";
    user: LocalUser;
    products: ProductWithImage[];
    sizeOfPage: number = 12;
    sortBy: string;
    pageNumber = 0;
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
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get("page"))).subscribe({
            next: param => {
                if (param && !isNaN(Number(param))) {
                    const number = Number(param) - 1;
                    this.pageNumber = number > 0? number : 0;
                }
            }
        })
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get("size"))).subscribe({
            next: param => {
                if (param && !isNaN(Number(param))) {
                    this.sizeOfPage = Number(param);
                }
            }
        })
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get("sortBy"))).subscribe({
            next: param => {
                if(param){
                    this.sortBy = param;
                }
            }
        })
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get("isAsc"))).subscribe({
            next: param => {
                this.isSortASC = param === 'true';
            }
        })
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get("isFilteredByWithPhoto"))).subscribe({
            next: param => {
                this.isFilteredByWithPhoto = param === 'true';
            }
        })
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get("isFilteredByVerified"))).subscribe({
            next: param => {
                this.isFilteredByVerified = param === 'true';
            }
        })
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get('search'))).subscribe({
            next: param => {
                if(param != this.searchWord) {
                    this.searchWord = param; //need to inject from resolver to prevent double fetching
                    this.getProducts(0);
                }
            }
        });
        this.activatedRoute.data.subscribe(data => {
            this.page = data['productsPage'];
            this.products = this.page.content;
        });
    }
    ngOnChanges() {
        console.log("XDS")
    }

    getProducts(page?: number) {
        if(page != null) {
            this.pageNumber = page;
        }
        this.productService.getProductList(this.pageNumber, this.sizeOfPage, true, this.sortBy, this.isSortASC, this.isFilteredByWithPhoto, this.isFilteredByVerified, this.searchWord).subscribe({
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
