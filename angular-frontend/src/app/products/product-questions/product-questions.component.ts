import {Component, OnInit} from '@angular/core';
import {Question} from "../../models/question";
import {ProductService} from "../../service/product.service";
import {ActivatedRoute} from "@angular/router";
import {ProductWithImage} from "../../models/product-with-image";

@Component({
    selector: 'app-product-questions',
    templateUrl: './product-questions.component.html',
    styleUrls: ['../product-details/product-details.component.css']
})
export class ProductQuestionsComponent implements OnInit {
    constructor(
        private productService: ProductService,
        private activatedRoute: ActivatedRoute
    ) {}

    isWriteQuestion = false;
    messageOfQuestion: string;
    questions: Question[];
    product: ProductWithImage;
    ngOnInit(): void {
        this.productService.getProductById(this.activatedRoute.snapshot.params['id']).subscribe({
            next: product => {
                this.product = product;
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
}
