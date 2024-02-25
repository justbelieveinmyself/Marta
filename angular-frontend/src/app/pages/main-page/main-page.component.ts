import {Component} from '@angular/core';
import {TokenService} from "../../services/token.service";
import {LocalUser} from "../../models/local-user";
import {ProductService} from "../../services/product.service";
import {ProductWithImage} from "../../models/product-with-image";
import {ErrorInterceptService} from "../../services/error-intercept.service";
import {Page} from "../../models/page";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {map} from "rxjs";
import {PageDataService} from "../../services/page-data.service";
import {fakeAsync} from "@angular/core/testing";

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
    isItemsBig = true;

    shownCategoryFilterBlock: boolean = false;
    shownSortFilterBlock: boolean = false;
    shownPriceFilterBlock: boolean = false;

    filterCategory: string;
    filterSortBy: string;
    filterPriceFrom: number = 0;
    filterPriceTo: number = 0;

    categories: string[] =
        ["Women's clothing", "Men's clothing", "Children's clothing", "Outerwear",
            "Footwear", "Sporting goods", "Accessories", "Cosmetics and perfumes",
            "Household Goods", "Electronics", "Children's goods and toys",
            "Beauty and health products", "Bags and accessories", "Watch", "Jewelry",
            "Gourmet products", "Books and audiobooks", "Sports nutrition",
            "Automotive products", "Furniture and household goods",
            "Tools and automotive supplies", "Pet Supplies"
        ]; //TODO: get from localstorage or from serv and save to localstorage
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

    resetPageData() {
        this.pageDataService.resetFilters();
        this.getProducts(0);
    }

    closeAllFilters(event: any) {

        if(event.target.classList.contains("dropdown-filter__btn") || event.target.localName === "input") {
            return;
        }
        this.shownCategoryFilterBlock = false;
        this.shownSortFilterBlock = false;
        this.shownPriceFilterBlock = false;
    }
}
