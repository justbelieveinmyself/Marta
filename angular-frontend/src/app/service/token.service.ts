import {Injectable, OnInit, SecurityContext} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';
import {LocalUser} from '../models/local-user';
import {EncryptionService} from './encryption.service';
import {Product} from "../models/product";
import {ProductWithImage} from "../models/product-with-image";
import {ImageService} from "./image.service";
import {DomSanitizer} from "@angular/platform-browser";

const ACCESS_TOKEN_KEY = 'AccessToken';
const REFRESH_TOKEN_KEY = 'RefreshToken';
const USER_KEY = 'AuthUser';
const PRODUCT_FAVOURITES_KEY = 'FavouritesImages';

@Injectable({
    providedIn: 'root'
})
export class TokenService {
    loggedIn = new BehaviorSubject<boolean>(this.getRefreshToken() ? true : false)

    constructor(
        private encryptionService: EncryptionService,
        private imageService: ImageService
    ) {}
    user: LocalUser;
    public setRefreshToken(refreshToken: string): void {
        window.localStorage.removeItem(REFRESH_TOKEN_KEY);
        window.localStorage.setItem(REFRESH_TOKEN_KEY, this.encryptionService.encryptData(refreshToken));
        this.loggedIn.next(true);
    }

    public getRefreshToken(): string {
        var encryptedToken = window.localStorage.getItem(REFRESH_TOKEN_KEY) || '';
        return this.encryptionService.decryptData(encryptedToken);
    }

    public setAccessToken(accessToken: string): void {
        window.sessionStorage.removeItem(ACCESS_TOKEN_KEY);
        window.sessionStorage.setItem(ACCESS_TOKEN_KEY, this.encryptionService.encryptData(accessToken));
        this.loggedIn.next(true);
    }

    public getAccessToken(): string {
        var encryptedToken = window.sessionStorage.getItem(ACCESS_TOKEN_KEY) || '';
        return this.encryptionService.decryptData(encryptedToken);
    }

    public setUser(user: LocalUser) {
        window.sessionStorage.removeItem(USER_KEY);
        var json = JSON.stringify(user);
        window.sessionStorage.setItem(USER_KEY, this.encryptionService.encryptData(json));
    }

    public getUser(): LocalUser {
        if(this.user == null) {
            var encryptedUser = sessionStorage.getItem(USER_KEY) || '';
            this.user = JSON.parse(this.encryptionService.decryptData(encryptedUser));
            this.user.favouriteProducts.map((product: ProductWithImage) => this.imageService.createImageInProduct(product));
        }
        return this.user;
    }

    public isLogged() {
        return this.loggedIn.asObservable();
    }

    public logOut(): void {
        window.sessionStorage.clear();
        window.localStorage.clear();
        this.user = null;
        this.loggedIn.next(false);
    }

}
