import {Component} from '@angular/core';
import {TokenService} from "../service/token.service";
import {LocalUser} from "../models/local-user";
import {ProductService} from "../service/product.service";
import {ProductWithImage} from "../models/product-with-image";
import {ErrorInterceptService} from "../service/error-intercept.service";
import {Page} from "../models/page";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {map, Observable} from "rxjs";

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
    searchWordObs: Observable<string>;
    searchWord: string;
    user: LocalUser;
    products: ProductWithImage[];
    sizeOfPage: number = 12;
    sortBy: string;
    page: Page<ProductWithImage>;
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
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get('search'))).subscribe({
            next: param => {
                this.searchWord = param
                this.getProducts(0);
            }
        });
        // this.getProducts(0);
        this.activatedRoute.data.subscribe(data => {
            // 'productsPage' - это ключ, который мы использовали при конфигурации резольвера
            this.page = data['productsPage'];
            // Теперь у вас есть доступ к данным из резольвера в свойстве this.page
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

    filterByVerified(event: any) {
        this.isFilteredByVerified = event.target.checked;
        this.getProducts(0);
    }

    protected readonly Array = Array;

    filterByWithPhoto(event: any) {
        this.isFilteredByWithPhoto = event.target.checked;
        this.getProducts(0);
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

    protected readonly console = console;
}
