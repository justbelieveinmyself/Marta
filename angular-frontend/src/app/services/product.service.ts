import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {forkJoin, map, mergeMap, Observable, tap} from 'rxjs';
import {Product} from '../models/product';
import {ProductWithImage} from '../models/product-with-image';
import {ImageService} from "./image.service";
import {Review} from "../models/review";
import {Question} from "../models/question";
import {Order} from "../models/order";
import {Page} from "../models/page";
import {ProductDetail} from "../models/product-detail";

@Injectable({
    providedIn: 'root'
})
export class ProductService {

    private baseUrl = "http://localhost:8080/api/v1/products";

    constructor(
        private httpClient: HttpClient,
        private imageService: ImageService
    ) {}

    getProductPages(
        page: number, size: number, sortBy: string, isAsc: boolean,
        isFilteredByWithPhoto: boolean, isFilteredByVerified: boolean, searchWord: string
    ): Observable<Page<ProductWithImage>> {
        if (!searchWord) {
            searchWord = "";
        }
        let url = `${this.baseUrl}/page?page=${page}&size=${size}&sortBy=${sortBy}&isAsc=${isAsc}&filterPhotoNotNull=${isFilteredByWithPhoto}&filterVerified=${isFilteredByVerified}&searchWord=${searchWord}`;

        return this.httpClient.get<Page<ProductWithImage>>(url).pipe(
            tap(page => page.content.forEach(product =>
                this.imageService.createImageInProduct(product)))
        );
    }

    getProductList(sellerId: number): Observable<ProductWithImage[]> {
        return this.httpClient.get<ProductWithImage[]>(this.baseUrl + sellerId).pipe(
            tap(productWithImage => productWithImage.forEach(product =>
                this.imageService.createImageInProduct(product)))
        );
    }

    getProductById(id: number): Observable<ProductWithImage> {
        return this.httpClient.get<ProductWithImage>(`${this.baseUrl}/${id}`).pipe(
            tap(product => this.imageService.createImageInProduct(product))
        );
    }

    getProductDetailById(id: number): Observable<ProductDetail> {
        return this.httpClient.get<ProductDetail>(`${this.baseUrl}/detail/${id}`).pipe(
            mergeMap(product => {
                const imagePromises = product.images.map(img =>
                    this.imageService.createUrlFromBase64(img)
                );
                return forkJoin(imagePromises).pipe(
                    map(urls => {
                        product.images = urls;
                        return product;
                    })
                );
            })
        );
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

    addProduct(product: Product, preview: File, productDetail: ProductDetail): Observable<Object> {
        const fd = new FormData();
        fd.append("file", preview);
        let jsonProduct = new Blob([JSON.stringify(product)], {type: 'application/json'});
        let jsonProductDetail = new Blob([JSON.stringify(productDetail)], {type: 'application/json'});
        fd.append("product", jsonProduct);
        fd.append("productDetail", jsonProductDetail);
        return this.httpClient.post(this.baseUrl, fd);
    }

    createOrder(productAndQuantity: Map<ProductWithImage, number>, isPaid: boolean): Observable<Order> {
        const productIdAndQuantity: { [id: string]: number } = {};
        productAndQuantity.forEach((value, key) => productIdAndQuantity[key.product.id.toString()] = value);
        const order = new Order();
        order.productIdAndQuantity = productIdAndQuantity;
        order.isPaid = isPaid;
        return this.httpClient.post<Order>(`http://localhost:8080/api/v1/orders`, order);
    }

    addProductToCart(product: Product): Observable<Product> {
        return this.httpClient.post<Product>(`${this.baseUrl}/cart/` + product.id, null);
    }

    addProductToFavourite(productId: number) {
        return this.httpClient.post<Product>(`${this.baseUrl}/favourite/` + productId, null);
    }

    addReview(review: Review, photos: File[]): Observable<Review> {
        let fd = new FormData();
        let json = new Blob([JSON.stringify(review)], {type: 'application/json'});
        fd.append("reviewDto", json);
        if (photos) {
            for (let i = 0; i < photos.length; i++) {
                fd.append("photos", photos[i]);
            }
        }
        return this.httpClient.post<Review>(`${this.baseUrl}/reviews`, fd);
    }

    addQuestion(question: Question): Observable<Question> {
        return this.httpClient.post<Question>(`${this.baseUrl}/questions`, question);
    }

    updateProduct(product: Product): Observable<Object> {
        return this.httpClient.put(this.baseUrl + '/' + product.id, product);
    }

    updateAnswerToReview(reviewId: number, answer: string): Observable<Review> {
        return this.httpClient.put<Review>(this.baseUrl + "/reviews/" + reviewId, answer);
    }

    verifyProduct(id: number): Observable<Object> {
        return this.httpClient.put(this.baseUrl + "/verify/" + id, null);
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

    deleteProductFromFavourite(productId: number): Observable<Object> {
        return this.httpClient.delete(`${this.baseUrl}/favourite/` + productId)
    }

}
