import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {LocalUser} from '../models/local-user';
import {EncryptionService} from './encryption.service';
import {ProductWithImage} from "../models/product-with-image";
import {ImageService} from "./image.service";

const ACCESS_TOKEN_KEY = 'AccessToken';
const REFRESH_TOKEN_KEY = 'RefreshToken';
const USER_KEY = 'AuthUser';

@Injectable({
    providedIn: 'root'
})
export class TokenService {
    loggedIn = new BehaviorSubject<boolean>(this.getRefreshToken() ? true : false)

    constructor(
        private encryptionService: EncryptionService
    ) {}

    private user: LocalUser;

    public setRefreshToken(refreshToken: string): void {
        window.localStorage.removeItem(REFRESH_TOKEN_KEY);
        window.localStorage.setItem(REFRESH_TOKEN_KEY, this.encryptionService.encryptData(refreshToken));
        this.loggedIn.next(true);
    }

    public getRefreshToken(): string {
        let encryptedToken = window.localStorage.getItem(REFRESH_TOKEN_KEY) || '';
        return this.encryptionService.decryptData(encryptedToken);
    }

    public setAccessToken(accessToken: string): void {
        window.sessionStorage.removeItem(ACCESS_TOKEN_KEY);
        window.sessionStorage.setItem(ACCESS_TOKEN_KEY, this.encryptionService.encryptData(accessToken));
        this.loggedIn.next(true);
    }

    public getAccessToken(): string {
        let encryptedToken = window.sessionStorage.getItem(ACCESS_TOKEN_KEY) || '';
        return this.encryptionService.decryptData(encryptedToken);
    }

    public setUser(user: LocalUser) {
        window.sessionStorage.removeItem(USER_KEY);
        let json = JSON.stringify(user);
        window.sessionStorage.setItem(USER_KEY, this.encryptionService.encryptData(json));
    }

    public getUser(): LocalUser {
        if (this.user == null) {
            let encryptedUser = sessionStorage.getItem(USER_KEY) || '';
            if (encryptedUser) {
                this.user = JSON.parse(this.encryptionService.decryptData(encryptedUser));
            } else {
                return null;
            }
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
