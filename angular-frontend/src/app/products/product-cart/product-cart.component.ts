import {Component, OnInit} from '@angular/core';
import {TokenService} from "../../service/token.service";
import {ProductService} from "../../service/product.service";
import {Product} from "../../models/product";
import {ProductWithImage} from "../../models/product-with-image";

@Component({
  selector: 'app-product-cart',
  templateUrl: './product-cart.component.html',
  styleUrls: ['./product-cart.component.css', '../product-details/product-details.component.css']
})
export class ProductCartComponent implements OnInit{
    constructor(
        private tokenService : TokenService,
        private productService: ProductService
    ) {}
    isLogged = false;
    products : ProductWithImage[];
    totalPrice = 0;
    ngOnInit(): void {
        this.tokenService.isLogged().subscribe(data => this.isLogged = data);
        this.productService.getProductsFromCart().subscribe({
            next: products => {
                this.products = products;
                this.products.forEach(product => this.totalPrice+=product.product.price);
                console.log(this.totalPrice)
            },
            error: err => console.log(err)
        })
    }

}
