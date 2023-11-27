import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Order} from "../models/order";
import {Observable, tap} from "rxjs";
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
        return this.httpClient.get<Order[]>(this.baseUrl)
            .pipe(tap(
                orders => {
                    orders.forEach(order => {
                        const productsAndQuantity: Map<ProductWithImage, number> = new Map();
                        let map: Map<string, number> = new Map(Object.entries(order.productIdAndQuantity));
                        if (map instanceof Map) {
                            map.forEach((value, key) => {
                                const numericKey: number = Number(key);
                                this.productService.getProductById(numericKey).subscribe(result => productsAndQuantity.set(result, value));
                            });
                            order.productsAndQuantity = productsAndQuantity;
                        }
                    })
                }));

    }
}
