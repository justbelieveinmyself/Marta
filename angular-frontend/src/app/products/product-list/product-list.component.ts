import { Component } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../service/product.service';
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
    this.productService.getProductList().subscribe({
      next: data =>{
      this.products = data;
    },
    error: e => {
      console.log(e);
    }
  });
  }
  updateProduct(id: number){
    this.router.navigate(['update-product/',id]);
  }
  deleteProduct(id: number){
    this.productService.deleteProduct(id).subscribe(data => {
        this.getProducts();
      });
  }
}
