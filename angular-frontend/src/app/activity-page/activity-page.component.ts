import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {LocalUser} from "../models/local-user";
import {UserService} from "../service/user.service";
import {ProductWithImage} from "../models/product-with-image";
import {ProductService} from "../service/product.service";

@Component({
    selector: 'app-activity-page',
    templateUrl: './activity-page.component.html',
    styleUrls: ['./activity-page.component.css']
})
export class ActivityPageComponent implements OnInit {
    constructor(
        private activatedRoute: ActivatedRoute,
        private userService: UserService,
        private productService: ProductService
    ) {}

    user: LocalUser;
    products: ProductWithImage[];
    ngOnInit(): void {
        this.userService.getUserCurrentOrById(this.activatedRoute.snapshot.params['id']).subscribe({
            next: user => {
                this.user = user;
                this.productService.getProductList(0, 1, false).subscribe({
                    next: page => {
                        this.products = page.content.filter(prod => prod.product.seller.username == this.user.username);
                    }
                })
            }
        })
    }

}
