import {Component, OnInit} from '@angular/core';
import {UserService} from "../service/user.service";
import {ProductWithImage} from "../models/product-with-image";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-user-favourites',
  templateUrl: './user-favourites.component.html',
  styleUrls: ['./user-favourites.component.css']
})
export class UserFavouritesComponent implements OnInit{
    constructor(
        private activatedRoute: ActivatedRoute
    ) {}
    favouriteProducts: ProductWithImage[];
    ngOnInit(): void {
        this.activatedRoute.data.subscribe(data => {
            this.favouriteProducts = data["favouritesProducts"];
        })

    }
}
