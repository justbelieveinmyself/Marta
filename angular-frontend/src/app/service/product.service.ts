import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, tap} from 'rxjs';
import {Product} from '../models/product';
import {ProductWithImage} from '../models/product-with-image';
import {ImageService} from "./image.service";
import {Review} from "../models/review";
import {Question} from "../models/question";
import {Order} from "../models/order";

@Injectable({
    providedIn: 'root'
})
export class ProductService {

    private baseUrl = "http://localhost:8080/api/v1/products";

    constructor(
        private httpClient: HttpClient,
        private imageService: ImageService
    ) {
    }

    getProductList(): Observable<ProductWithImage[]> {
        return this.httpClient.get<ProductWithImage[]>(this.baseUrl).pipe(tap(products =>
            products.map(product => this.imageService.createImageInProduct(product))));
    }

    getProductById(id: number): Observable<ProductWithImage> {
        return this.httpClient.get<ProductWithImage>(`${this.baseUrl}/${id}`)
            .pipe(tap(product => this.imageService.createImageInProduct(product)));
    }

    getProductReviews(productId: number): Observable<Review[]> {
        return this.httpClient.get<Review[]>(`${this.baseUrl}/reviews/` + productId);
    }

    getProductsFromCart(): Observable<ProductWithImage[]> {
        return this.httpClient.get<ProductWithImage[]>(`${this.baseUrl}/cart`).pipe(tap(products =>
            products.map(
                product => this.imageService.createImageInProduct(product))
            )
        );
    }

    getProductQuestions(productId: number): Observable<Question[]> {
        return this.httpClient.get<Question[]>(`${this.baseUrl}/questions/` + productId);
    }

    addProduct(product: Product, preview: File): Observable<Object> {
        const fd = new FormData();
        fd.append("file", preview);
        var json = new Blob([JSON.stringify(product)], {type: 'application/json'});
        fd.append("product", json);
        return this.httpClient.post(this.baseUrl, fd);
    }

    createOrder(productAndQuantity : Map<ProductWithImage, number>, isPaid: boolean): Observable<Order> {
        const productIdAndQuantity: {[id: string]: number} = {};
        productAndQuantity.forEach((value, key) => productIdAndQuantity[key.product.id.toString()]=value);
        const order = new Order();
        order.productIdAndQuantity = productIdAndQuantity;
        order.isPaid = isPaid;
        return this.httpClient.post<Order>(`http://localhost:8080/api/v1/orders`, order);
    }

    addProductToCart(product: Product): Observable<Product>{
        return this.httpClient.post<Product>(`${this.baseUrl}/cart/` + product.id, null);
    }

    addProductToFavourite(product: Product){
        return this.httpClient.post<Product>(`${this.baseUrl}/favourite/` + product.id, null);
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

    addQuestion(question : Question) : Observable<Question>{
        return this.httpClient.post<Question>(`${this.baseUrl}/questions`, question);
    }

    updateProduct(product: Product): Observable<Object> {
        return this.httpClient.put(this.baseUrl + '/' + product.id, product);
    }

    deleteProduct(id: number): Observable<Object> {
        return this.httpClient.delete(`${this.baseUrl}/${id}`);
    }

    deleteAllProductsInCart(): Observable<Object> {
        return this.httpClient.delete(`${this.baseUrl}/cart`);
    }

    deleteProductInCart(product: Product): Observable<Object> {
        return this.httpClient.delete(`${this.baseUrl}/cart/${product.id}`);
    }

    deleteProductFromFavourite(product: Product): Observable<Object>{
        return this.httpClient.delete(`${this.baseUrl}/favourite/` + product.id)
    }

}
