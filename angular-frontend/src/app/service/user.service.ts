import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = "http://localhost:8080/oauth";
  constructor(private httpClient: HttpClient) { }
  getUserFromDbByOauth(userId: string, token: string){
    const tokenDto = {
      userId, token
    }
    this.httpClient.post(this.baseUrl, tokenDto).subscribe(data =>
      console.log(data)
    );
  }
}
