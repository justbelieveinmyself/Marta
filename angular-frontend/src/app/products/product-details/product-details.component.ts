import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product.service";
import {ActivatedRoute} from "@angular/router";
import {ProductWithImage} from "../../models/product-with-image";
import {ImageService} from "../../service/image.service";
import {map} from "rxjs";
import {HttpClient, HttpResponse} from "@angular/common/http";

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
    isReviews = true;
    isAppearing = false;
    isNeedLeftButton = false;
    isReceivedProduct = true;
    isWriteQuestion = false;
    ngOnInit(): void {
        this.productService.getProductById(this.activatedRoute.snapshot.params['id']).subscribe({
            next: product => this.product = product,
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
}
