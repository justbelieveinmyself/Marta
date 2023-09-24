import { Component } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent {
  products: Product[] = [];
  
  constructor(private productService : ProductService,
    private router: Router){}
  
  ngOnInit(): void {
    this.getProducts();
  }
  private getProducts(){
    this.productService.getProductList().subscribe(data =>{
      this.products = data;
    });
  }
  updateProduct(id: number){
    this.router.navigate(['update-product/',id])
  }
}
