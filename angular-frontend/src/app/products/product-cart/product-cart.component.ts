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

    addNumberOfProduct(product: Product, index: number){
        this.countOfProduct[index]++;
        this.totalPrice += product.price;
        this.totalCountOfProduct++;
    }
    removeNumberOfProduct(product: Product, index: number){
        if(this.countOfProduct[index] > 1) {
            this.countOfProduct[index]--;
            this.totalPrice -= product.price;
            this.totalCountOfProduct--;
        }
    }
    deleteProductInCart(product: Product, index: number){
        this.productService.deleteProductInCart(product).subscribe({
            next: result => {
                this.totalPrice -= product.price * this.countOfProduct[index];
                this.totalCountOfProduct -= this.countOfProduct[index];
                this.products = this.products.filter(prod => prod.product != product);
                this.countOfProduct.splice(index, 1);
            },
            error: err => console.log(err)
        })
    }
}
