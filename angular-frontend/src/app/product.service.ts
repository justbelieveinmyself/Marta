import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from './product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  
  private baseUrl = "http://localhost:8080/product";

  constructor(private httpClient: HttpClient) { }

  getProductList() : Observable<Product[]>{
    return this.httpClient.get<Product[]>(this.baseUrl);
  }
}