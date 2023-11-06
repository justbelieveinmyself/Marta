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

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private authUrl = "http://localhost:8080/auth/";

    constructor(
        private httpClient: HttpClient,
        private imageService: ImageService
    ) {
    }


    public register(regUser: RegisterUser, avatar: File): Observable<any> {
        var fd = new FormData();
        var json = new Blob([JSON.stringify(regUser)], {type: 'application/json'});
        fd.append("regUser", json);
        fd.append("file", avatar);
        return this.httpClient.post<any>(this.authUrl + 'register', fd);
    }

    public async login(logUser: LoginUser): Promise<LoginResponseDto> {
        const dto = await firstValueFrom(this.httpClient.post<LoginResponseDto>(this.authUrl + 'login', logUser));
        dto.user.favouriteProducts = await Promise.all(dto.user.favouriteProducts.map(async (product) => {
            return await this.imageService.createImageInProduct(product);
        }));
        console.log(dto.user.favouriteProducts);
        return dto;
    }

    public getAccessToken(refreshToken: string): Observable<RefreshResponseDto> {
        const dto = {refreshToken};
        return this.httpClient.post<any>(this.authUrl + 'refresh', dto);
    }
}
