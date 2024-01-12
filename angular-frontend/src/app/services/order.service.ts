import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Order} from "../models/order";
import {catchError, forkJoin, map, Observable, of, switchMap, tap} from "rxjs";
import {ProductService} from "./product.service";
import {ProductWithImage} from "../models/product-with-image";

@Injectable({
    providedIn: 'root'
})
export class OrderService {
    private baseUrl = "http://localhost:8080/api/v1/orders";

    constructor(
        private httpClient: HttpClient,
        private productService: ProductService
    ) {}

    getCurrentUserOrders(): Observable<Order[]> {
    return this.httpClient.get<Order[]>(this.baseUrl).pipe(
        switchMap(orders => {
            const observables: Observable<ProductWithImage>[] = [];

            orders.forEach(order => {
                const productsAndQuantity: Map<ProductWithImage, number> = new Map();
                const map: Map<string, number> = new Map(Object.entries(order.productIdAndQuantity));

                if (map instanceof Map) {
                    map.forEach((value, key) => {
                        const numericKey: number = Number(key);
                        const productObservable = this.productService.getProductById(numericKey).pipe(
                            tap(result => productsAndQuantity.set(result, value)),
                            catchError(error => {
                                console.error(`Error fetching product ${numericKey}: ${error}`);
                                return of(null);
                            })
                        );
                        observables.push(productObservable);
                    });
                }

                order.productsAndQuantity = productsAndQuantity;
            });

            return forkJoin(observables).pipe(map(() => orders));
        })
    );
}


}
