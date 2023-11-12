import {Component, Input, OnInit} from '@angular/core';
import {ProductWithImage} from 'src/app/models/product-with-image';
import {delay} from "rxjs";

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html',
    styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit{
    @Input() card: ProductWithImage[];
    @Input() isOrders: boolean;
    ngOnInit() {
    }
}
