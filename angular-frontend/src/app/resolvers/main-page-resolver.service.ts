import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {ProductWithImage} from "../models/product-with-image";
import {Page} from "../models/page";
import {ProductService} from "../service/product.service";

@Injectable({
    providedIn: 'root'
})
export class MainPageResolverService implements Resolve<Page<ProductWithImage>> {
    constructor(private productService: ProductService) {}

    resolve(route: ActivatedRouteSnapshot): Observable<any> {
        const page = route.queryParams['page'] || 0;
        const size = route.queryParams['size'] || 12;
        const sortBy = route.queryParams['sortBy'];
        const isAsc = route.queryParams['isAsc'] === 'true';
        const isFilteredByWithPhoto = route.queryParams['isFilteredByWithPhoto'] === 'true';
        const isFilteredByVerified = route.queryParams['isFilteredByVerified'] === 'true';
        const searchWord = route.queryParams['searchWord'];
        return this.productService.getProductList(page, size, true, sortBy, isAsc, isFilteredByWithPhoto, isFilteredByVerified, searchWord);
    }
}
