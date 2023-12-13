import {Component, OnInit} from '@angular/core';
import {ProductWithImage} from "../../models/product-with-image";
import {ProductService} from "../../service/product.service";
import {ActivatedRoute} from "@angular/router";
import {Review} from "../../models/review";
import {ImageService} from "../../service/image.service";
import {UserService} from "../../service/user.service";
import {OrderService} from "../../service/order.service";
import {LocalUser} from "../../models/local-user";
import {TokenService} from "../../service/token.service";
import {ImageModel} from "../../models/image-model";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
    selector: 'app-product-feedback',
    templateUrl: './product-feedback.component.html',
    styleUrls: ['../product-details/product-details.component.css']
})
export class ProductFeedbackComponent implements OnInit {
    constructor(
        private productService: ProductService,
        private activatedRoute: ActivatedRoute,
        private imageService: ImageService,
        private sanitizer: DomSanitizer,
        private userService: UserService,
        private tokenService: TokenService
    ) {}

    protected readonly Math = Math;
    product: ProductWithImage;
    currentUser: LocalUser;
    reviews: Review[];
    filteredReviews: Review[];
    isReceivedProduct = true;
    isFavourite = false;
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
    countOfProductInOrder = 1;
    reviewInAnswerToReview: Review;
    currentRate: number = 0;
    totalPrice: number;
    isPaid = false;
    messageOfReview: string;
    answerForReview: string;
    reviewPhotos: ImageModel[] = [];

    ngOnInit(): void {
        this.productService.getProductById(this.activatedRoute.snapshot.params['id']).subscribe({
            next: product => {
                this.product = product;
                this.totalPrice = this.product.product.price;
                this.userService.getFavourites().subscribe(favourites => {
                    this.isFavourite = favourites.filter(prod => prod.product.id == this.product.product.id).length != 0;
                })
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
                            this.countOfReviewsWithPhotos += review.photos.length > 0 ? 1 : 0;
                            review.photos = urls;

                        })
                        this.isNeedRightButtonForPhotos = this.countOfPhotos > 11; //TODO
                        this.filteredReviews = reviews;
                        if (this.isNeedSortByDate) this.sortByDate();
                        this.isNeedOpenAnswer = new Array(this.filteredReviews.length).fill(false);
                    },
                    error: err => console.log(err)
                })
                this.currentUser = this.tokenService.getUser();
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

    filterReviewsByWithPhotos(event: any) {
        this.isNeedReviewsOnlyWithPhotos = event.target.checked;
        this.isNeedOpenAnswer.fill(false);
        if (this.isNeedReviewsOnlyWithPhotos) {
            this.filteredReviews = this.reviews.filter(review => review.photos.length > 0)
        } else {
            this.filteredReviews = this.reviews;
        }
    }

    sortByDate() {
        this.isNeedSortByRate = false;
        this.isNeedSortByDate = true;
        this.isNeedOpenAnswer.fill(false);
        this.sortByDateASC = !this.sortByDateASC;
        if (this.sortByDateASC) {
            this.filteredReviews.sort((review, review2) => new Date(review.time).getTime() - new Date(review2.time).getTime())
        } else {
            this.filteredReviews.sort((review, review2) => new Date(review2.time).getTime() - new Date(review.time).getTime())
        }
    }

    sortByRate() {
        this.isNeedSortByDate = false;
        this.isNeedSortByRate = true;
        this.sortByRateASC = !this.sortByRateASC;
        this.isNeedOpenAnswer.fill(false);
        if (this.sortByRateASC) {
            this.filteredReviews.sort((review, review2) => review.rating - review2.rating);
        } else {
            this.filteredReviews.sort((review, review2) => review2.rating - review.rating);
        }
    }

    reportReview() {
    }

    addToCart(){
        this.productService.addProductToCart(this.product.product).subscribe({
            error: err => console.log(err)
        });
        // @ts-ignore
        document.getElementById('liveToast').classList.add("show");
    }
    addOrRemoveFavourite() { //TODO: remove duplicated code
        if(this.isFavourite){
            this.productService.deleteProductFromFavourite(this.product.product).subscribe({
                error: err => console.log(err)
            })
        }else {
            this.productService.addProductToFavourite(this.product.product).subscribe({
                error: err => console.log(err)
            });
        }
        this.isFavourite = !this.isFavourite;
    }

    orderNow() { //TODO: remove duplicated code
        const map = new Map<ProductWithImage, number>;
        map.set(this.product, this.countOfProductInOrder);
        this.productService.createOrder(map, this.isPaid).subscribe({
            next: result => {
                this.totalPrice = this.product.product.price;
                this.countOfProductInOrder = 1;
            },
            error: err => console.log(err)
        })
    }

    addNumberOfProduct() {
        this.countOfProductInOrder++;
        this.totalPrice += this.product.product.price;
    }

    removeNumberOfProduct() {
        if(this.countOfProductInOrder > 1){
            this.countOfProductInOrder--;
            this.totalPrice -= this.product.product.price;
        }
    }

    onFileAdded(event: any) {
        if (event.target.files) {
            const file = event.target.files[0];
            const imageModel: ImageModel = {
                file: file,
                url: this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(file))
            }
            this.reviewPhotos.push(imageModel);
        }
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
                if (review.photos) {
                    let urls: string[] = [];
                    review.photos.map(photo => {
                        this.imageService.createUrlFromBase64(photo).then(url =>
                            urls.push(url)
                        )
                    });
                    review.photos = urls;
                }
                this.reviews.push(review);
            },
            error: err => console.log(err)
        });
    }

    saveAnswerToReview(){
        console.log(this.reviewInAnswerToReview.id)
        this.productService.updateAnswerToReview(this.reviewInAnswerToReview.id, this.answerForReview).subscribe({
            next: review => {
                this.reviewInAnswerToReview.answer = this.answerForReview;
            }
        })
    }

    protected readonly console = console;
}
