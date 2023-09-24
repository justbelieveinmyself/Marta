import { Component } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.css']
})
export class CreateProductComponent {
  product: Product = new Product();
  
  constructor(private productService: ProductService,
    private router: Router){}

  onSubmit(){
    this.saveProduct();
    this.redirectToProductList();
  }
  saveProduct(){
    this.productService.addProduct(this.product).subscribe(data => {
      console.log(data)
    });
  }
  redirectToProductList(){
    this.router.navigate(['/products']);
  }
}
