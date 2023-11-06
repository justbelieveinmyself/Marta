import {Component, OnInit} from '@angular/core';
import {LocalUser} from "../models/local-user";
import {TokenService} from "../service/token.service";

@Component({
  selector: 'app-user-favourites',
  templateUrl: './user-favourites.component.html',
  styleUrls: ['./user-favourites.component.css']
})
export class UserFavouritesComponent implements OnInit{
    constructor(private tokenService: TokenService) {
    }
    user: LocalUser;
    ngOnInit(): void {
        this.user = this.tokenService.getUser();
    }
}
