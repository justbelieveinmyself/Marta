import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ProductService} from "../../../services/product.service";
import {ActivatedRoute} from "@angular/router";
import {ProductWithImage} from "../../../models/product-with-image";
import {Review} from "../../../models/review";
import {ImageService} from "../../../services/image.service";
import {ImageModel} from "../../../models/image-model";
import {DomSanitizer} from "@angular/platform-browser";
import {Question} from "../../../models/question";
import {UserService} from "../../../services/user.service";
import {ProductInteractionService} from "../../../services/product-interaction.service";
import {ProductDetail} from "../../../models/product-detail";
import {Carousel} from "../../../models/carousel";

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
        private userService: UserService,
        private productInteractionService: ProductInteractionService
    ) {}

    carousel: Carousel<string>;
    card: ProductWithImage;
    productDetail: ProductDetail;
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

    shownPopup: boolean = false;
    sellerTooltip: {shown: boolean, posX: number, posY: number} = {shown: false, posX: 0, posY: 0}
    @ViewChild("asideContainer", {static: true}) asideContainer: ElementRef;

    ngOnInit(): void {
        this.activatedRoute.data.subscribe(data => {
            this.card = data["product"];
            this.productDetail = data["productDetail"];
            this.carousel = new Carousel(this.productDetail.images);
            console.log(this.productDetail)
            this.totalPrice = this.card.product.price;
        });
        this.productService.getProductReviews(this.card.product.id).subscribe({
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
                this.userService.getFavourites().subscribe(favourites => {
                    this.isFavourite = favourites.filter(prod => prod.product.id == this.card.product.id).length != 0;
                })

            },
            error: err => console.log(err)
        });
        this.productService.getProductQuestions(this.card.product.id).subscribe({
            next: questions => {
                this.questions = questions.filter(question => question.answer != null);
                this.isNeedRightButtonForQuestions = this.questions.length > 3; //TODO
            },
            error: err => console.log(err)
        })

    }

    addToCart() {
        this.productService.addProductToCart(this.card.product).subscribe({
            next: result => console.log(123),
            error: err => console.log(err)
        });
        // @ts-ignore
        document.getElementById('liveToast').classList.add("show");
    }

    addOrRemoveFavourite() {
        this.productInteractionService.toggleFavourite(this.isFavourite, this.card.product.id)
            .subscribe(isFavourite => {
                this.isFavourite = isFavourite;
            });
    }

    saveReview() {
        this.productInteractionService.saveReview(this.messageOfReview, this.currentRate, this.card.product.id, this.reviewPhotos)
            .then(review => {
                this.reviews.push(review);
            })
    }

    saveQuestion() {
        const question = new Question();
        question.message = this.messageOfQuestion;
        question.productId = this.card.product.id;
        this.productService.addQuestion(question).subscribe({
            next: question => console.log("added new unanswered question", question), //need to be answered for display
            error: err => console.log(err)
        })
    }

    reportReview() {
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
        this.productInteractionService.orderNow(this.card, this.countOfProductInOrder, this.isPaid).then(isOrdered => {
            if (isOrdered) {
                this.countOfProductInOrder = 1;
                this.totalPrice = this.card.product.price;
            }
        });
    }

    addNumberOfProduct() {
        this.countOfProductInOrder++;
        this.totalPrice += this.card.product.price;
    }

    removeNumberOfProduct() {
        if (this.countOfProductInOrder > 1) {
            this.countOfProductInOrder--;
            this.totalPrice -= this.card.product.price;
        }
    }

    copyToClipboard(text: string) {
        navigator.clipboard.writeText(text);
    }

    goToLink(templateUrl: string) {
        const formattedText = this.card.product.productName.replaceAll(" ", "%20");
        let splitedTemplate = templateUrl.split("url=");
        window.open(splitedTemplate[0] + "url=" + window.location.href + (splitedTemplate[1] ? splitedTemplate[1] : "") + formattedText);
    }

    toggleOverflowBody() {
        if (this.shownPopup) {
            document.body.classList.add("body--overflow")
        } else {
            document.body.classList.remove("body--overflow")
        }
    }

    protected readonly window = window;

    toggleSellerTooltip(event: any) {
        if (this.sellerTooltip.shown) {
            this.sellerTooltip.shown = false;
        } else {
            this.sellerTooltip.shown = true;
            const rect = this.asideContainer.nativeElement.getBoundingClientRect();
            this.sellerTooltip.posX = rect.left;
            this.sellerTooltip.posY = event.pageY - rect.height;
        }
    }
}
