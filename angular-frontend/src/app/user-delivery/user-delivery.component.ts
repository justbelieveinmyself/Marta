import {Component, OnInit} from '@angular/core';
import {OrderService} from "../service/order.service";
import {Order} from "../models/order";
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-user-delivery',
    templateUrl: './user-delivery.component.html',
    styleUrls: ['./user-delivery.component.css']
})
export class UserDeliveryComponent implements OnInit {
    constructor(
        private activatedRoute: ActivatedRoute
    ) {}

    orders: Order[];
    totalSum: number;

    ngOnInit(): void {
        this.activatedRoute.data.subscribe(data => {
            const orders: Order[] = data["orders"];
            this.orders = orders.sort((order, order2) => new Date(order.orderedAt).getTime() - new Date(order2.orderedAt).getTime())
            this.totalSum = this.countTotalSum();
        })
    }

    countTotalSum(){
        return this.orders.filter(order => order.isPaid == false)
            .reduce((sum, order) => {
                order.productsAndQuantity.forEach((quantity, product) => {
                    sum += product.product.price * quantity
                });
                return sum;
            }, 0);
    }

    protected readonly Array = Array;
}
