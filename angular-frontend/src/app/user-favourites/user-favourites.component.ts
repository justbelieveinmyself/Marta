import {Component, OnInit} from '@angular/core';
import {UserService} from "../service/user.service";
import {ProductWithImage} from "../models/product-with-image";

@Component({
  selector: 'app-user-favourites',
  templateUrl: './user-favourites.component.html',
  styleUrls: ['./user-favourites.component.css']
})
export class UserFavouritesComponent implements OnInit{
    constructor(
        private userService: UserService
    ) {}
    favouriteProducts: ProductWithImage[];
    ngOnInit(): void {
        this.userService.getFavourites().subscribe({
            next: products => {
                this.favouriteProducts = products
            },
            error: err => console.log(err)
        });

    }
}
