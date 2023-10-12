import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterUser } from '../models/register-user';
import { Observable } from 'rxjs';
import { LoginUser } from '../models/login-user';
import { LoginResponseDto } from '../models/login-response.dto';
import {RefreshResponseDto} from "../models/refresh-response.dto";
import {Dto} from "../models/dto";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = "http://localhost:8080/auth/";
  constructor(private httpClient : HttpClient) { }


  public register(regUser : RegisterUser, avatar : File) : Observable<any>{
    var fd = new FormData();
    var json = new Blob([JSON.stringify(regUser)], { type: 'application/json'});
    fd.append("regUser", json);
    fd.append("file", avatar);
    return this.httpClient.post<any>(this.authUrl + 'register', fd);
  }

  public login(logUser : LoginUser) : Observable<LoginResponseDto>{
    return this.httpClient.post<any>(this.authUrl + 'login', logUser);
  }

  public getAccessToken(refreshToken : string) : Observable<RefreshResponseDto>{
    const dto = new Dto(refreshToken);
    return this.httpClient.post<any>(this.authUrl + 'refresh', dto);
  }
}
