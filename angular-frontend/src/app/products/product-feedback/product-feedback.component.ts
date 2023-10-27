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

    protected readonly Math = Math;
    product: ProductWithImage;
    reviews: Review[];
    filteredReviews: Review[];
    isNeedLeftButtonForPhotos = false;
    isNeedRightButtonForPhotos = false;
    isNeedReviewsOnlyWithPhotos = false;
    isNeedSortByDate = true;
    sortByDateASC = true;
    sortByRateASC = true;
    isNeedSortByRate = false;
    isNeedOpenAnswer: boolean[] = [];
    countOfPhotos = 0;
    countOfReviewsWithPhotos = 0;
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
                            this.countOfReviewsWithPhotos += review.photos.length > 0? 1 : 0;
                            review.photos = urls;

                        })
                        this.isNeedRightButtonForPhotos = this.countOfPhotos >11; //TODO
                        this.filteredReviews = reviews;
                        if(this.isNeedSortByDate) this.sortByDate();
                        this.isNeedOpenAnswer = new Array(this.filteredReviews.length).fill(false);
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
        this.isNeedReviewsOnlyWithPhotos = event.target.checked;
        this.isNeedOpenAnswer.fill(false);
        if(this.isNeedReviewsOnlyWithPhotos){
            this.filteredReviews = this.reviews.filter(review => review.photos.length > 0)
        }else{
            this.filteredReviews = this.reviews;
        }
    }
    sortByDate(){
        this.isNeedSortByRate = false;
        this.isNeedSortByDate = true;
        this.isNeedOpenAnswer.fill(false);
        this.sortByDateASC = !this.sortByDateASC;
        if(this.sortByDateASC){
            this.filteredReviews.sort((review, review2) => new Date(review.time).getTime() - new Date(review2.time).getTime())
        }else{
            this.filteredReviews.sort((review, review2) => new Date(review2.time).getTime() - new Date(review.time).getTime())
        }


    }
    sortByRate(){
        this.isNeedSortByDate = false;
        this.isNeedSortByRate = true;
        this.sortByRateASC = !this.sortByRateASC;
        this.isNeedOpenAnswer.fill(false);
        if(this.sortByRateASC){
            this.filteredReviews.sort((review, review2) => review.rating - review2.rating);
        }else{
            this.filteredReviews.sort((review, review2) => review2.rating - review.rating);
        }

    }

}
