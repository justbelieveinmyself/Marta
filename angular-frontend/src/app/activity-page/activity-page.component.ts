import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {LocalUser} from "../models/local-user";
import {ProductWithImage} from "../models/product-with-image";

@Component({
    selector: 'app-activity-page',
    templateUrl: './activity-page.component.html',
    styleUrls: ['./activity-page.component.css']
})
export class ActivityPageComponent implements OnInit {
    constructor(
        private activatedRoute: ActivatedRoute
    ) {}

    user: LocalUser;
    products: ProductWithImage[];
    ngOnInit(): void {
        this.activatedRoute.data.subscribe(data => {
            this.user = data["user"];
            this.products = data["products"].content.filter((prod: ProductWithImage) => prod.product.seller.username == this.user.username);
        });
    }
}
