import {Component, OnInit} from '@angular/core';
import {TokenService} from "../../service/token.service";

@Component({
  selector: 'app-product-cart',
  templateUrl: './product-cart.component.html',
  styleUrls: ['./product-cart.component.css', '../product-details/product-details.component.css']
})
export class ProductCartComponent implements OnInit{
    constructor(
        private tokenService : TokenService
    ) {}
    isLogged = false;
    ngOnInit(): void {
        this.tokenService.isLogged().subscribe(data => this.isLogged = data);
    }

}
