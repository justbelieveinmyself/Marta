import {Component, OnInit} from '@angular/core';
import {Question} from "../../models/question";
import {ProductService} from "../../service/product.service";
import {ActivatedRoute} from "@angular/router";
import {ProductWithImage} from "../../models/product-with-image";
import {UserService} from "../../service/user.service";

@Component({
    selector: 'app-product-questions',
    templateUrl: './product-questions.component.html',
    styleUrls: ['../product-details/product-details.component.css']
})
export class ProductQuestionsComponent implements OnInit {
    constructor(
        private productService: ProductService,
        private activatedRoute: ActivatedRoute,
        private userService: UserService
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
        this.productService.getProductById(this.activatedRoute.snapshot.params['id']).subscribe({
            next: product => {
                this.product = product;
                this.totalPrice = product.product.price;
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

    addToCart(){
        this.productService.addProductToCart(this.product.product).subscribe({
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

}
