import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product.service";
import {ActivatedRoute} from "@angular/router";
import {ProductWithImage} from "../../models/product-with-image";
import {Review} from "../../models/review";
import {ImageService} from "../../service/image.service";
import {resolve} from "@angular/compiler-cli";
import {ImageModel} from "../../models/image-model";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
    selector: 'app-product-details',
    templateUrl: './product-details.component.html',
    styleUrls: ['./product-details.component.css'],
    animations: []
})
export class ProductDetailsComponent implements OnInit {
    protected readonly Math = Math;

    constructor(
        private productService: ProductService,
        private activatedRoute: ActivatedRoute,
        private imageService: ImageService,
        private sanitizer: DomSanitizer
    ) {
    }

    product: ProductWithImage;
    reviews: Review[];
    isReviews = true;
    isAppearing = false;
    isNeedLeftButton = false;
    isReceivedProduct = true;
    isWriteQuestion = false;
    currentRate: number = 0;
    messageOfReview: string;
    reviewPhotos: ImageModel[] = [];
    ngOnInit(): void {
        this.productService.getProductById(this.activatedRoute.snapshot.params['id']).subscribe({
            next: product => {
                this.product = product;
                this.productService.getProductReviews(this.product.product.id).subscribe({
                    next: reviews => {
                        this.reviews = reviews;
                        this.reviews.forEach(review => {
                            let urls: string[] = [];
                            review.photos.map(photo => {
                                this.imageService.createUrlFromBase64(photo).then(url =>
                                    urls.push(url)
                                )
                            });
                            review.photos = urls;
                        })
                    },
                    error: err => console.log(err)
                });
            },
            error: err => console.log(err)
        });
    }

    rightScroll() {
        // @ts-ignore
        scrollReviews.scrollLeft += 440;
        // @ts-ignore
        this.isNeedLeftButton = scrollReviews.scrollLeft + 440 > 0;
    }

    leftScroll() {
        // @ts-ignore
        scrollReviews.scrollLeft -= 440;
        // @ts-ignore
        this.isNeedLeftButton = scrollReviews.scrollLeft - 440 > 0;
    }

    saveReview() {
        let review = new Review();
        review.message = this.messageOfReview;
        review.rating = this.currentRate;
        review.productId = this.product.product.id;
        let files = null;
        if (this.reviewPhotos) {
            files = this.reviewPhotos.map(photos => photos.file);
        }
        // @ts-ignore
        this.productService.addReview(review, files).subscribe({
            next: review => {
                let urls: string[] = [];
                review.photos.map(photo => {
                    this.imageService.createUrlFromBase64(photo).then(url =>
                        urls.push(url)
                    )
                });
                review.photos = urls;
                this.reviews.push(review);
            },
            error: err => console.log(err)
        });
    }

    onFileAdded(event : any){
        if(event.target.files){
            const file = event.target.files[0];
            const imageModel : ImageModel = {
                file: file,
                url: this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(file))
            }
            this.reviewPhotos.push(imageModel);
        }
    }
}
