import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product.service";
import {ActivatedRoute} from "@angular/router";
import {ProductWithImage} from "../../models/product-with-image";
import {Review} from "../../models/review";
import {ImageService} from "../../service/image.service";
import {ImageModel} from "../../models/image-model";
import {DomSanitizer} from "@angular/platform-browser";
import {Question} from "../../models/question";
import {UserService} from "../../service/user.service";

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
        private sanitizer: DomSanitizer,
        private userService: UserService
    ) {}

    product: ProductWithImage;
    reviews: Review[];
    questions: Question[];
    isReviews = true;
    isAppearing = false;
    isFavourite = false;
    isNeedLeftButtonForReviews = false;
    isNeedRightButtonForReviews = false;
    isNeedLeftButtonForQuestions = false;
    isNeedRightButtonForQuestions = false;
    isReceivedProduct = true;
    isWriteQuestion = false;
    ratingOverall: number = 0;
    currentRate: number = 0;
    messageOfReview: string;
    messageOfQuestion: string;
    reviewPhotos: ImageModel[] = [];
    countOfProductInOrder = 1;
    totalPrice: number;
    isPaid = false;
    ngOnInit(): void {
        this.productService.getProductById(this.activatedRoute.snapshot.params['id']).subscribe({
            next: product => {
                this.product = product;
                this.totalPrice = product.product.price;
                this.productService.getProductReviews(this.product.product.id).subscribe({
                    next: reviews => {
                        this.reviews = reviews;
                        let sumRate = 0;
                        reviews.forEach(rev => sumRate += rev.rating);
                        this.ratingOverall = sumRate / reviews.length;
                        this.reviews.forEach(review => {
                            let urls: string[] = [];
                            review.photos.map(photo => {
                                this.imageService.createUrlFromBase64(photo).then(url =>
                                    urls.push(url)
                                )
                            });
                            review.photos = urls;
                        })
                        this.isNeedRightButtonForReviews = this.reviews.length > 2;
                        this.isNeedRightButtonForQuestions = this.questions.length > 3; //TODO
                        this.userService.getFavourites().subscribe(favourites => {
                            this.isFavourite = favourites.filter(prod => prod.product.id == this.product.product.id).length != 0;
                        })

                    },
                    error: err => console.log(err)
                });
                this.productService.getProductQuestions(this.product.product.id).subscribe({
                    next: questions => {
                        this.questions = questions.filter(question => question.answer != null);
                    },
                    error: err => console.log(err)
                })
            },
            error: err => console.log(err)
        });
    }

    addToCart(){
        this.productService.addProductToCart(this.product.product).subscribe({
            next: result => console.log(123),
            error: err => console.log(err)
        });
        // @ts-ignore
        document.getElementById('liveToast').classList.add("show");
    }

    addOrRemoveFavourite() {
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

    saveQuestion() {
        const question = new Question();
        question.message = this.messageOfQuestion;
        question.productId = this.product.product.id;
        this.productService.addQuestion(question).subscribe({
            next: question => console.log("added new unanswered question", question), //need to be answered for display
            error: err => console.log(err)
        })
    }

    reportReview(){}

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

    rightScrollForReviews() {
        // @ts-ignore
        scrollReviews.scrollLeft += 440;
        // @ts-ignore
        this.isNeedLeftButtonForReviews = scrollReviews.scrollLeft + 440 > 0;
        // @ts-ignore
        this.isNeedRightButtonForReviews = scrollReviews.scrollWidth - 1296 > scrollReviews.scrollLeft + 440;
    }

    leftScrollForReviews() {
        // @ts-ignore
        scrollReviews.scrollLeft -= 440;
        // @ts-ignore
        this.isNeedLeftButtonForReviews = scrollReviews.scrollLeft - 440 > 0;
        // @ts-ignore
        this.isNeedRightButtonForReviews = scrollReviews.scrollWidth - 1296 > scrollReviews.scrollLeft - 440;
    }

    rightScrollForQuestions() {
        // @ts-ignore
        scrollQuestions.scrollLeft += 440;
        // @ts-ignore
        this.isNeedLeftButtonForQuestions = scrollQuestions.scrollLeft + 440 > 0;
        // @ts-ignore
        this.isNeedRightButtonForQuestions = scrollQuestions.scrollWidth - 1296 > scrollQuestions.scrollLeft + 440;
    }

    leftScrollForQuestions() {
        // @ts-ignore
        scrollQuestions.scrollLeft -= 440;
        // @ts-ignore
        this.isNeedLeftButtonForQuestions = scrollQuestions.scrollLeft - 440 > 0;
        // @ts-ignore
        this.isNeedRightButtonForQuestions = scrollQuestions.scrollWidth - 1296 > scrollQuestions.scrollLeft - 440;
    }

    orderNow() {
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

    copyToClipboard() {
        navigator.clipboard.writeText(window.location.href);
    }

    goToLink(templateUrl: string) {
        const fomattedText = this.product.product.productName.replaceAll(" ", "%20");
        let splitedTemplate = templateUrl.split("url=");
        window.open(splitedTemplate[0] + "url=" + window.location.href + (splitedTemplate[1]? splitedTemplate[1] : "") + fomattedText);
    }

}
