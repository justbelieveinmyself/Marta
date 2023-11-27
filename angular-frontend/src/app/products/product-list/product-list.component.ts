import {Component, Input} from '@angular/core';
import {ProductWithImage} from 'src/app/models/product-with-image';
import {Order} from "../../models/order";
import {ProductService} from "../../service/product.service";
import {Product} from "../../models/product";

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html',
    styleUrls: ['./product-list.component.css']
})
export class ProductListComponent{
    constructor(
        private productService: ProductService
    ) {}
    @Input() card: ProductWithImage[];
    @Input() isOrders: boolean;
    @Input() orders: Order[];
    products: ProductWithImage[];
    productInToast: Product;
    ngOnInit() {
        if(!this.card && !this.orders){
            setTimeout(() => this.ngOnInit(), 50);
        }
        this.products = this.card;
    }
    addToCart(product: Product){
        this.productService.addProductToCart(product).subscribe({
            error: err => console.log(err)
        });
        this.productInToast = product;
        console.log(this.productInToast)
        // @ts-ignore
        document.getElementById('liveToast').classList.add("show");
    }

    verifyProduct(product: Product, index: number){
        this.productService.verifyProduct(product.id).subscribe({
            next: res => {
                this.card.at(index).product.isVerified = true;
            }
        })
    }

    protected readonly Array = Array;

}
