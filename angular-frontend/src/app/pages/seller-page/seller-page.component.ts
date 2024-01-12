import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProductWithImage} from "../../models/product-with-image";

@Component({
  selector: 'app-seller-page',
  templateUrl: './seller-page.component.html',
  styleUrls: ['./seller-page.component.css']
})
export class SellerPageComponent {
    constructor(
        private activatedRoute: ActivatedRoute
    ) {}

    products: ProductWithImage[];
    ngOnInit() {
        this.activatedRoute.data.subscribe(data => {
            this.products = data["products"];
        })
    }
}
