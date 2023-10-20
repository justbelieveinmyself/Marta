import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product.service";
import {ActivatedRoute} from "@angular/router";
import {ProductWithImage} from "../../models/product-with-image";
import {Review} from "../../models/review";

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
        private activatedRoute: ActivatedRoute
    ) {}

    product: ProductWithImage;
    reviews: Review[];
    isReviews = true;
    isAppearing = false;
    isNeedLeftButton = false;
    isReceivedProduct = true;
    isWriteQuestion = false;
    currentRate: number = 0;
    messageOfReview: string;
    ngOnInit(): void {
        this.productService.getProductById(this.activatedRoute.snapshot.params['id']).subscribe({
            next: product => {
                this.product = product;
                this.productService.getProductReviews(this.product.product.id).subscribe({
                    next: reviews => this.reviews = reviews,
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

    saveReview(){
        let review = new Review();
        review.message = this.messageOfReview;
        review.rating = this.currentRate;
        review.productId = this.product.product.id;
        // review.photos = base64,.......
        this.productService.addReview(review).subscribe({
            next: review => this.reviews.push(review),
            error: err => console.log(err)
        });
    }
}
