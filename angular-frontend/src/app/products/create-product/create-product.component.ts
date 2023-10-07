import { Component, OnInit } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../service/product.service';
import { Router } from '@angular/router';
import { TokenService } from 'src/app/service/token.service';
import { LocalUser } from 'src/app/models/local-user';

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.css']
})
export class CreateProductComponent implements OnInit {
  product: Product = new Product();
  user!: LocalUser;
  previewImg!: File;
  constructor(
    private productService: ProductService,
    private router: Router,
    private tokenService: TokenService
    ){}
  ngOnInit(){
    this.user = this.tokenService.getUser();
  }
  onSubmit(){
    this.saveProduct();
    
  }
  saveProduct(){
    this.product.seller = this.user;
    this.productService.addProduct(this.product, this.previewImg).subscribe(data => {
      console.log(data);
      this.redirectToProductList();
    });
  }
  redirectToProductList(){
    this.router.navigate(['/products']);
  }
  onFileSelected(event: any){
    this.previewImg = event.target.files[0];
  }
}
