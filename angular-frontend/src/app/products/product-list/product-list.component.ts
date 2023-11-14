import {Component, Input, OnInit} from '@angular/core';
import {ProductWithImage} from 'src/app/models/product-with-image';
import {Order} from "../../models/order";
import {ProductService} from "../../service/product.service";
import {Product} from "../../models/product";

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html',
    styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit{
    constructor(
        private productService: ProductService
    ) {}
    @Input() card: ProductWithImage[];
    @Input() isOrders: boolean;
    @Input() orders: Order[];
    ngOnInit() {
    }
    productInToast: Product;
    addToCart(product: Product){
        this.productService.addProductToCart(product).subscribe({
            error: err => console.log(err)
        });
        this.productInToast = product;
        console.log(this.productInToast)
        // @ts-ignore
        document.getElementById('liveToast').classList.add("show");
    }
    protected readonly Array = Array;
}
