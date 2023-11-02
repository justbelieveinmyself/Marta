import {Component, OnInit} from '@angular/core';
import {TokenService} from "../../service/token.service";
import {ProductService} from "../../service/product.service";
import {Product} from "../../models/product";
import {ProductWithImage} from "../../models/product-with-image";
import {LocalUser} from "../../models/local-user";

@Component({
  selector: 'app-product-cart',
  templateUrl: './product-cart.component.html',
  styleUrls: ['./product-cart.component.css', '../product-details/product-details.component.css']
})
export class ProductCartComponent implements OnInit{
    constructor(
        private tokenService: TokenService,
        private productService: ProductService,
    ) {}
    isLogged = false;
    user: LocalUser;
    products: ProductWithImage[];
    countOfProduct: number[];
    totalCountOfProduct = 0;
    totalPrice = 0;
    ngOnInit(): void {
        this.tokenService.isLogged().subscribe(data => this.isLogged = data);
        this.productService.getProductsFromCart().subscribe({
            next: products => {
                this.products = products;
                this.products.forEach(product => this.totalPrice+=product.product.price);
                this.countOfProduct = new Array(this.products.length).fill(1);
                this.totalCountOfProduct = this.products.length;
            },
            error: err => console.log(err)
        })
        this.user = this.tokenService.getUser();
    }

    addNumberOfProduct(event : any){
        const id = event.target.id;
        this.countOfProduct[id]++;
        this.totalPrice+=this.products[id].product.price;
        this.totalCountOfProduct++;
    }
    removeNumberOfProduct(event : any){
        const id = event.target.id;
        if(this.countOfProduct[id] > 1) {
            this.countOfProduct[id]--;
            this.totalPrice -= this.products[id].product.price;
            this.totalCountOfProduct--;
        }
    }
}
