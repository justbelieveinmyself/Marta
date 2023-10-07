import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  
  private baseUrl = "http://localhost:8080/product";

  constructor(private httpClient: HttpClient) { }

  getProductList() : Observable<Product[]>{
    return this.httpClient.get<Product[]>(this.baseUrl);
  }
  addProduct(product : Product, preview : File) : Observable<Object>{
    const fd = new FormData();
    fd.append("file", preview);
    var json = new Blob([JSON.stringify(product)], { type: 'application/json'});
    fd.append("product", json);
    return this.httpClient.post(this.baseUrl, fd);
  }
  updateProduct(product : Product) : Observable<Object>{
    return this.httpClient.put(this.baseUrl +'/' +product.id, product);
  }
  getProductById(id: number) : Observable<Product>{
    return this.httpClient.get<Product>(`${this.baseUrl}/${id}`);
  }
  deleteProduct(id: number) : Observable<Object>{
    return this.httpClient.delete(`${this.baseUrl}/${id}`);
  }

}