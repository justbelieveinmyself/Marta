import { Injectable, OnInit } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { LocalUser } from '../models/local-user';
import { EncryptionService } from './encryption.service';

const TOKEN_KEY = 'AuthToken';
const USER_KEY = 'AuthUser';
@Injectable({
  providedIn: 'root'
})
export class TokenService {
  loggedIn = new BehaviorSubject<boolean>(this.getToken()? true: false)
  constructor(
    private encryptionService : EncryptionService
  ) { }

  public setToken(token : string) : void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, this.encryptionService.encryptData(token));
    this.loggedIn.next(true);
  }

  public getToken() : string{
    var encryptedToken = window.sessionStorage.getItem(TOKEN_KEY) || '';
    return this.encryptionService.decryptData(encryptedToken);
  }

  public setUser(user: LocalUser){
    window.sessionStorage.removeItem(USER_KEY);
    var json = JSON.stringify(user);
    window.sessionStorage.setItem(USER_KEY, this.encryptionService.encryptData(json));
  }

  public getUser() : LocalUser{
    var encryptedUser = sessionStorage.getItem(USER_KEY) || '';
    return JSON.parse(this.encryptionService.decryptData(encryptedUser));
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
