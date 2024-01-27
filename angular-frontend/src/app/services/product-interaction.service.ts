import {Injectable} from '@angular/core';
import {ProductWithImage} from "../models/product-with-image";
import {ProductService} from "./product.service";
import {Review} from "../models/review";
import {ImageModel} from "../models/image-model";
import {ImageService} from "./image.service";
import {map, Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ProductInteractionService {
    constructor(
        private productService: ProductService,
        private imageService: ImageService
    ) {}

    async orderNow(product: ProductWithImage, countOfProduct: number, isPaid: boolean): Promise<boolean> {
        const map = new Map<ProductWithImage, number>();
        map.set(product, countOfProduct);
        return new Promise((resolve) => {
            this.productService.createOrder(map, isPaid).subscribe({
                next: result => {
                    resolve(true);
                },
                error: err => {
                    console.error(err);
                    resolve(false);
                }
            });
        });
    }

    async saveReview(message: string, rate: number, productId: number, photos?: ImageModel[]): Promise<Review> {
        let review = new Review();
        review.message = message;
        review.rating = rate;
        review.productId = productId;
        let files: File[] = null;
        if (photos) {
            files = photos.map(photo => photo.file);
        }
        return new Promise((resolve, reject) => {
            this.productService.addReview(review, files).subscribe({
                next: review => {
                    if (review.photos) {
                        Promise.all(review.photos.map(photo => this.imageService.createUrlFromBase64(photo)))
                            .then(urls => {
                                review.photos = urls;
                                resolve(review);
                            })
                            .catch(err => reject(err));
                    } else {
                        resolve(review);
                    }
                },
                error: err => reject(err)
            });
        });
    }

    toggleFavourite(isFavourite: boolean, productId: number): Observable<boolean> {
        if (isFavourite) {
            return this.productService.deleteProductFromFavourite(productId).pipe(
                map(() => false)
            );
        } else {
            return this.productService.addProductToFavourite(productId).pipe(
                map(() => true)
            );
        }
    }

}
