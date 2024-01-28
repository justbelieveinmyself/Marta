import {Component, Input} from '@angular/core';
import {Order} from "../../../models/order";
import {Seller} from "../../../models/seller";

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent {
    @Input() orders: Order[];
    contactSeller: Seller = new Seller();
    messageForSeller = "";

    sendMessageToSeller() {

    }
}
