import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {map, Observable} from 'rxjs';
import {Product} from '../models/product';
import {ProductWithImage} from '../models/product-with-image';
import {ImageService} from "./image.service";
import {Review} from "../models/review";

@Injectable({
    providedIn: 'root'
})
export class ProductService {

    private baseUrl = "http://localhost:8080/products";

    constructor(
        private httpClient: HttpClient,
        private imageService: ImageService
    ) {
    }

    getProductList(): Observable<ProductWithImage[]> {
        return this.httpClient.get<ProductWithImage[]>(this.baseUrl).pipe(map(products =>
            products.map(product => this.imageService.createImageInProduct(product))));
    }

    getProductReviews(productId: number): Observable<Review[]> {
        return this.httpClient.get<Review[]>(`${this.baseUrl}/reviews/` + productId);
    }

    addProduct(product: Product, preview: File): Observable<Object> {
        const fd = new FormData();
        fd.append("file", preview);
        var json = new Blob([JSON.stringify(product)], {type: 'application/json'});
        fd.append("product", json);
        return this.httpClient.post(this.baseUrl, fd);
    }

    addReview(review: Review, photos: File[]): Observable<Review> {
        var fd = new FormData();
        var json = new Blob([JSON.stringify(review)], {type: 'application/json'});
        fd.append("reviewDto", json);
        if (photos) {
            for (let i = 0; i < photos.length; i++) {
                fd.append("photos", photos[i]);
            }
        }
        return this.httpClient.post<Review>(`${this.baseUrl}/reviews`, fd);
    }

    updateProduct(product: Product): Observable<Object> {
        return this.httpClient.put(this.baseUrl + '/' + product.id, product);
    }

    getProductById(id: number): Observable<ProductWithImage> {
        return this.httpClient.get<ProductWithImage>(`${this.baseUrl}/${id}`)
            .pipe(map(product => this.imageService.createImageInProduct(product)));
    }

    deleteProduct(id: number): Observable<Object> {
        return this.httpClient.delete(`${this.baseUrl}/${id}`);
    }

}
