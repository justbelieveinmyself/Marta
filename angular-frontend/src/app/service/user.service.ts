import { HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenService } from './token.service';
import { Observable } from 'rxjs';
import {LocalUser} from "../models/local-user";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private oauthUrl = "http://localhost:8080/oauth";
  private baseUrl = "http://localhost:8080/profiles";
  constructor(
    private httpClient: HttpClient,
    private tokenService: TokenService
    ) { }

  getUserFromDbByOauth(userId: string, token: string){
    const tokenDto = {
      userId, token
    }
    this.httpClient.post(this.oauthUrl, tokenDto).subscribe(data =>
      console.log(data)
    );
  }

  getAvatar(userid: number) : Observable<Blob>{
    return this.httpClient.get(`${this.baseUrl}/${userid}/avatar`, {
      responseType: 'blob'
    });
  }

  getUser() : Observable<LocalUser>{
    return this.httpClient.get<LocalUser>(this.baseUrl);
  }

  updateEmail(userid: number, email: string){
    this.httpClient.put(`${this.baseUrl}/${userid}/email`, email).subscribe((data : any) => {
      this.tokenService.setUser(data.user);
    });
  }

  updateAvatar(userid: number, avatar: File) : Observable<any> {
    const fd = new FormData();
    fd.append("file", avatar);
    return this.httpClient.put(`${this.baseUrl}/${userid}/avatar`, fd);
  }

  updateNames(userId: number, name: string, surname: string){
    const req = {firstName : name, lastName : surname};
    this.httpClient.put(`${this.baseUrl}/${userId}/nameAndSurname`, req)
      .subscribe((data : any) => {
      this.tokenService.setUser(data);
    });
  }

  updateGender(userId: number, gender: string){
    this.httpClient.put(`${this.baseUrl}/${userId}/gender`, gender).subscribe((data : any) => {
      this.tokenService.setUser(data);
    });
  }
}
