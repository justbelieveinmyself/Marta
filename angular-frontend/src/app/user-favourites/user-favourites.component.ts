import {Component, OnInit} from '@angular/core';
import {LocalUser} from "../models/local-user";
import {TokenService} from "../service/token.service";
import {UserService} from "../service/user.service";
import {ProductWithImage} from "../models/product-with-image";

@Component({
  selector: 'app-user-favourites',
  templateUrl: './user-favourites.component.html',
  styleUrls: ['./user-favourites.component.css']
})
export class UserFavouritesComponent implements OnInit{
    constructor(
        private tokenService: TokenService,
        private userService: UserService
    ) {}
    favouriteProducts: ProductWithImage[];
    ngOnInit(): void {
        this.userService.getFavourites().subscribe({
            next: products => {
                this.favouriteProducts = products
                console.log(this.favouriteProducts)
            },
            error: err => console.log(err)
        });

    }
}
