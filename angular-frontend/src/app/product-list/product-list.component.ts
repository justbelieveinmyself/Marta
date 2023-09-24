import { Component } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent {
  products: Product[] = [];
  
  constructor(private productService : ProductService){}
  
  ngOnInit(): void {
    this.getProducts();
  }
  private getProducts(){
    this.productService.getProductList().subscribe(data =>{
      this.products = data;
    });
  }
}
