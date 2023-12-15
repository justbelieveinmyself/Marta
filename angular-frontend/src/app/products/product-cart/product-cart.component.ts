import {Component, OnInit} from '@angular/core';
import {TokenService} from "../../service/token.service";
import {ProductService} from "../../service/product.service";
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
    isPayNow = false;
    user: LocalUser;
    productAndQuantity = new Map<ProductWithImage, number>;
    totalCountOfProduct = 0;
    totalPrice = 0;
    ngOnInit(): void {
        this.tokenService.isLogged().subscribe(data => this.isLogged = data);
        this.productService.getProductsFromCart().subscribe({
            next: products => {
                products.forEach(product => this.productAndQuantity.set(product, 1));
                products.forEach(product => this.totalPrice += product.product.price);
                this.totalCountOfProduct = products.length;
            },
            error: err => console.log(err)
        })
        this.user = this.tokenService.getUser();
    }

    addNumberOfProduct(product: ProductWithImage){
        let quantity = this.productAndQuantity.get(product);
        this.productAndQuantity.set(product, ++quantity);
        this.totalPrice += product.product.price;
        this.totalCountOfProduct++;
    }

    removeNumberOfProduct(product: ProductWithImage){
        let quantity = this.productAndQuantity.get(product);
        if(quantity > 1) {
            quantity--;
            this.productAndQuantity.set(product, quantity);
            this.totalPrice -= product.product.price;
            this.totalCountOfProduct--;
        }
    }
    deleteProductInCart(product: ProductWithImage, index: number){
        this.productService.deleteProductInCart(product.product).subscribe({
            next: result => {
                let quantity = this.productAndQuantity.get(product);
                this.totalPrice -= product.product.price * quantity;
                this.totalCountOfProduct -= quantity;
                this.productAndQuantity.delete(product);
            },
            error: err => console.log(err)
        })
    }

    orderProducts() {
        this.productService.createOrder(this.productAndQuantity, this.isPayNow).subscribe({
            next: result => {
                this.productService.deleteAllProductsInCart().subscribe({
                    next: res => {
                        this.productAndQuantity.clear();
                        this.totalPrice = 0;
                        this.totalCountOfProduct = 0;
                    },
                    error: err => console.log()
                })
            },
            error: err => console.log(err)
        });
    }

    choosePayMethod() {

    }
}

