import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterUser } from '../models/register-user';
import { Observable } from 'rxjs';
import { LoginUser } from '../models/login-user';
import { JwtDto } from '../models/jwt-dto';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = "http://localhost:8080/auth/";
  constructor(private httpClient : HttpClient) { }

  public register(regUser : RegisterUser) : Observable<any>{
    return this.httpClient.post<any>(this.authUrl + 'register', regUser);
  }

  public login(logUser : LoginUser) : Observable<JwtDto>{
    return this.httpClient.post<any>(this.authUrl + 'login', logUser);
  }
}
