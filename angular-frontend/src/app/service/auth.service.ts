import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {RegisterUser} from '../models/register-user';
import {firstValueFrom, Observable, of, switchMap, tap} from 'rxjs';
import {LoginUser} from '../models/login-user';
import {LoginResponseDto} from '../models/login-response.dto';
import {RefreshResponseDto} from "../models/refresh-response.dto";
import {RefreshRequestDto} from "../models/refresh-request.dto";
import {daysToMonths} from "ngx-bootstrap/chronos/duration/bubble";
import {ImageService} from "./image.service";
import {UserService} from "./user.service";
import {TokenService} from "./token.service";
import {Router} from "@angular/router";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private authUrl = "http://localhost:8080/api/v1/auth/";

    constructor(
        private httpClient: HttpClient
    ) {}

    public register(regUser: RegisterUser, avatar: File): Observable<any> {
        var fd = new FormData();
        var json = new Blob([JSON.stringify(regUser)], {type: 'application/json'});
        fd.append("regUser", json);
        fd.append("file", avatar);
        return this.httpClient.post<any>(this.authUrl + 'register', fd);
    }

    public login(logUser: LoginUser): Observable<LoginResponseDto> {
        return this.httpClient.post<LoginResponseDto>(this.authUrl + 'login', logUser);
    }

    public getAccessToken(refreshToken: string): Observable<RefreshResponseDto> {
        const dto = {refreshToken};
        return this.httpClient.post<any>(this.authUrl + 'refresh', dto);
    }

}
