import {Component, OnInit} from '@angular/core';
import {OrderService} from "../service/order.service";
import {Order} from "../models/order";

@Component({
  selector: 'app-user-delivery',
  templateUrl: './user-delivery.component.html',
  styleUrls: ['./user-delivery.component.css']
})
export class UserDeliveryComponent implements OnInit{
    orders: Order[];
    constructor(
        private orderService: OrderService
    ) {}

    ngOnInit(): void {
        this.orderService.getCurrentUserOrders().subscribe(next => {
            this.orders = next
            console.log(this.orders)
        });

    }

}
