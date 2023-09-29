import { Injectable, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { LocalUser } from '../models/local-user';

const TOKEN_KEY = 'AuthToken';
const USER_KEY = 'AuthUser';
const TOKEN_TTL_MS = 1800000;
@Injectable({
  providedIn: 'root'
})
export class TokenService{
  loggedIn = new BehaviorSubject<boolean>(this.getToken()? true: false)
  constructor() { }

  public setToken(token : string) : void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
      // timeStamp : new Date().getTime()
    this.loggedIn.next(true);
  }

  public getToken() : string{
    return sessionStorage.getItem(TOKEN_KEY) || '';
  }

  public setUser(user: LocalUser){
    sessionStorage.removeItem(USER_KEY);
    sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser() : LocalUser{
    return JSON.parse(sessionStorage.getItem(USER_KEY) || '');
  }

  public isLogged() {
    return this.loggedIn.asObservable();
  }
  
  public logOut(): void {
    window.sessionStorage.clear();
    this.loggedIn.next(false);
  }
  
  // public isExpired(timeStamp: number){
  //   if(!timeStamp) return false;
  //   const now = new Date().getTime();
  //   const diff = now - timeStamp;
  //   return diff > TOKEN_TTL_MS; 
  // }
}
