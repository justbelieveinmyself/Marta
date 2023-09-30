import { Component } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../service/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { LocalUser } from 'src/app/models/local-user';
import { TokenService } from 'src/app/service/token.service';

@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html',
  styleUrls: ['./update-product.component.css']
})
export class UpdateProductComponent {
  user!: LocalUser;
  product! : Product;
  hasRights! : boolean;
  productId! : number;
  constructor(private productService : ProductService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private tokenService: TokenService){}
  ngOnInit(){
    this.user = this.tokenService.getUser();
    this.productId = this.activatedRoute.snapshot.params['id'];
    this.productService.getProductById(this.productId).subscribe({ 
      next: (p) =>  {
        this.product = p;
        this.hasRights = this.user.id == this.product.seller.id;
      }, 
      error: (e) => console.log(e)
    });

  }
  onSubmit(){
    this.product.seller = this.user;
    this.productService.updateProduct(this.product).subscribe(data => {
      console.log(data);
    });
    this.redirectToProductList();
  }
  redirectToProductList(){
    this.router.navigate(['products']);
  }
}
