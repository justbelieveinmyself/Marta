import { Component } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../service/product.service';
import { Router } from '@angular/router';
import { TokenService } from 'src/app/service/token.service';
import { LocalUser } from 'src/app/models/local-user';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent {
  products: Product[] = [];
  user!: LocalUser;
  constructor(
    private productService : ProductService,
    private router: Router,
    private tokenService: TokenService){}
  
  ngOnInit(): void {
    this.user = this.tokenService.getUser();
    this.getProducts();
  }
  private getProducts(){
    this.productService.getProductList().subscribe({
      next: data =>{
      this.products = data;
    },
    error: e => {
      if(e.status == 403){
        this.router.navigate(['/login']);
      }
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
