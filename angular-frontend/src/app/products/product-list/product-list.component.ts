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
    addToCart(product: Product){
        this.productService.addProductToCart(product).subscribe({
            error: err => console.log(err)
        });
        // @ts-ignore
        document.getElementById('liveToast').classList.add("show");
    }
    protected readonly Array = Array;
}
