import {Component, OnInit} from '@angular/core';
import {Question} from "../../../models/question";
import {ProductService} from "../../../services/product.service";
import {ActivatedRoute} from "@angular/router";
import {ProductWithImage} from "../../../models/product-with-image";
import {UserService} from "../../../services/user.service";
import {ProductInteractionService} from "../../../services/product-interaction.service";

@Component({
    selector: 'app-product-questions',
    templateUrl: './product-questions.component.html',
    styleUrls: ['../product-details/product-details.component.css']
})
export class ProductQuestionsComponent implements OnInit {
    constructor(
        private productService: ProductService,
        private activatedRoute: ActivatedRoute,
        private userService: UserService,
        private productInteractionService: ProductInteractionService
    ) {}

    isFavourite = false;
    isWriteQuestion = false;
    messageOfQuestion: string;
    questions: Question[];
    product: ProductWithImage;
    countOfProductInOrder = 1;
    totalPrice: number;
    isPaid = false;

    ngOnInit(): void {
        this.activatedRoute.data.subscribe(data => {
            this.product = data["product"];
        });
        this.totalPrice = this.product.product.price;
        this.userService.getFavourites().subscribe(favourites => {
            this.isFavourite = favourites.filter(prod => prod.product.id == this.product.product.id).length != 0;
            console.log(this.isFavourite)
        })
        this.productService.getProductQuestions(this.product.product.id).subscribe({
            next: questions => {
                this.questions = questions.filter(question => question.answer != null);
            },
            error: err => console.log(err)
        })
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

    addToCart() {
        this.productService.addProductToCart(this.product.product).subscribe({
            error: err => console.log(err)
        });
        // @ts-ignore
        document.getElementById('liveToast').classList.add("show");
    }

    addOrRemoveFavourite() {
        this.productInteractionService.addOrRemoveFavourite(this.isFavourite, this.product.product.id)
        .subscribe(isFavourite => {
            this.isFavourite = isFavourite;
        });
    }

    orderNow() {
        this.productInteractionService.orderNow(this.product, this.countOfProductInOrder, this.isPaid).then(isOrdered => {
            if(isOrdered) {
                this.countOfProductInOrder = 1;
                this.totalPrice = this.product.product.price;
            }
        });
    }

    addNumberOfProduct() {
        this.countOfProductInOrder++;
        this.totalPrice += this.product.product.price;
    }

    removeNumberOfProduct() {
        if (this.countOfProductInOrder > 1) {
            this.countOfProductInOrder--;
            this.totalPrice -= this.product.product.price;
        }
    }

}
