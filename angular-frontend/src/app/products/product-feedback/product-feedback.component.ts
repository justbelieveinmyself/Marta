import {Component, OnInit} from '@angular/core';
import {ProductWithImage} from "../../models/product-with-image";
import {ProductService} from "../../service/product.service";
import {ActivatedRoute} from "@angular/router";
import {Review} from "../../models/review";
import {ImageService} from "../../service/image.service";

@Component({
    selector: 'app-product-feedback',
    templateUrl: './product-feedback.component.html',
    styleUrls: ['../product-details/product-details.component.css']
})
export class ProductFeedbackComponent implements OnInit {
    constructor(
        private productService: ProductService,
        private activatedRoute: ActivatedRoute,
        private imageService: ImageService
    ) {
    }

    product: ProductWithImage;
    reviews: Review[];
    isNeedLeftButtonForPhotos = false;
    isNeedRightButtonForPhotos = false;
    countOfPhotos = 0;
    isNeedReviewsOnlyWithPhotos = false;
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
                                this.imageService.createUrlFromBase64(photo).then(url => {
                                        urls.push(url);

                                    }
                                )
                            });
                            this.countOfPhotos += review.photos.length;
                            review.photos = urls;

                        })
                        this.isNeedRightButtonForPhotos = this.countOfPhotos >11; //TODO
                    },
                    error: err => console.log(err)
                })
            },
            error: err => console.log(err)
        })
    }

    rightScrollForPhotos() {
        // @ts-ignore
        scrollPhotos.scrollLeft += 440;
        // @ts-ignore
        this.isNeedLeftButtonForPhotos = scrollPhotos.scrollLeft + 440 > 0;
        // @ts-ignore
        this.isNeedRightButtonForPhotos = scrollPhotos.scrollWidth - 1296 > scrollPhotos.scrollLeft + 440;
    }

    leftScrollForPhotos() {
        // @ts-ignore
        scrollPhotos.scrollLeft -= 440;
        // @ts-ignore
        this.isNeedLeftButtonForPhotos = scrollPhotos.scrollLeft - 440 > 0;
        // @ts-ignore
        this.isNeedRightButtonForPhotos = scrollPhotos.scrollWidth - 1296 > scrollPhotos.scrollLeft - 440;

    }

    onChangeFilterReviewsWithPhotos(event:any){
        this.isNeedReviewsOnlyWithPhotos = event.srcElement.checked;
        //TODO
    }

    protected readonly Math = Math;
}
