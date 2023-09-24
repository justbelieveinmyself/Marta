import { Component } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html',
  styleUrls: ['./update-product.component.css']
})
export class UpdateProductComponent {
  product : Product = new Product;
  constructor(private productService : ProductService,
    private router: Router,
    private activatedRoute: ActivatedRoute){}
  ngOnInit(){
    this.product.id = this.activatedRoute.snapshot.params['id'];
  }
  onSubmit(){
    this.productService.updateProduct(this.product).subscribe(data => {
      console.log(this.product);
    
    });
    this.redirectToProductList();
  }
  redirectToProductList(){
    this.router.navigate(['products']);
  }
}
