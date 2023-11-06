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
        private sanitizer: DomSanitizer
    ) {}
    isImagesCreated = false;
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
            this.user.favouriteProducts.map((product: ProductWithImage) => this.createImageInProduct(product));
        }
        return this.user;
    }
    createImageInProduct(product: ProductWithImage): ProductWithImage { //TODO: Remove circular dependency iamgeservice and userserivce.
        if (product.file == "") {
            product.file = "https://www.webstoresl.com/sellercenter/assets/images/no-product-image.png";
            return product;
        }
        var image;
        this.base64ToBlob(product.file).then(blob => {
            image = this.createUrlFromBlobProm(blob);
            return image;
        }).then(url => {
            product.file = url;
        });
        return product;
    }
    base64ToBlob(base64String: string): Promise<Blob> {
        return new Promise<Blob>((resolve, reject) => {
            const byteCharacters = atob(base64String);
            const byteArrays = [];
            for (let offset = 0; offset < byteCharacters.length; offset += 512) {
                const slice = byteCharacters.slice(offset, offset + 512);
                const byteNumbers = new Array(slice.length);
                for (let i = 0; i < slice.length; i++) {
                    byteNumbers[i] = slice.charCodeAt(i);
                }
                const byteArray = new Uint8Array(byteNumbers);
                byteArrays.push(byteArray);
            }
            const blob = new Blob(byteArrays, {type: 'image/jpeg'});
            resolve(blob);
        });
    }
    createUrlFromBlobProm(blob: Blob): Promise<string> {
        return new Promise<string>((resolve, reject) => {
            let result = URL.createObjectURL(blob);
            resolve(this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, this.sanitizer.bypassSecurityTrustResourceUrl(result))!);
        })
    }
    public isLogged() {
        return this.loggedIn.asObservable();
    }

    public logOut(): void {
        window.sessionStorage.clear();
        window.localStorage.clear();
        this.loggedIn.next(false);
    }

}
