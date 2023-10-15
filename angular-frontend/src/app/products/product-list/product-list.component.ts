import { Component } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../service/product.service';
import { Router } from '@angular/router';
import { TokenService } from 'src/app/service/token.service';
import { LocalUser } from 'src/app/models/local-user';
import { ProductWithImage } from 'src/app/models/product-with-image';
import { ImageService } from 'src/app/service/image.service';
import {AuthService} from "../../service/auth.service";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent {
  card: ProductWithImage[] = [];
  user!: LocalUser;
  constructor(
    private productService : ProductService,
    private router: Router,
    private tokenService: TokenService,
    private imageService: ImageService,
    private authService: AuthService,
    private userService: UserService){}

  ngOnInit(): void {
    try {
      this.user = this.tokenService.getUser();
      this.getProducts();
    }
    catch{
      if(this.tokenService.getRefreshToken() != null){
        this.authService.getAccessToken(this.tokenService.getRefreshToken()).subscribe({
          next: token => {
            this.tokenService.setAccessToken(token.accessToken);
            this.tokenService.setRefreshToken(token.refreshToken);
            this.getProducts();
            this.userService.getUser().subscribe({
              next: user => {
                this.tokenService.setUser(user);
                this.user = user;
              }
            });
          },
          error: error => {
            console.log("error")
            this.tokenService.logOut()
          }
        })
      }
    }

  }

  private getProducts(){
    this.productService.getProductList().subscribe({
      next: data =>{
      this.card = data;

      this.card.forEach(p => {
        if(p.file == "") {
          p.file = "https://www.webstoresl.com/sellercenter/assets/images/no-product-image.png";
          return;
        }
        var image;
        this.base64ToBlob(p.file).then(blob => {
          image = this.imageService.createUrlFromBlobProm(blob);
          return image;
        }).then(url => {
          p.file = url;
        });
      })
    },
    error: e => {
      console.log("1");
      this.updateAccess();
      this.router.navigate(['/login']);
    }
    });
  }
  updateAccess(){
    if(this.tokenService.getRefreshToken() != null){
      this.authService.getAccessToken(this.tokenService.getRefreshToken()).subscribe({
        next: token => {
          this.tokenService.setAccessToken(token.accessToken);
          this.tokenService.setRefreshToken(token.refreshToken);
          this.getProducts();
          this.userService.getUser().subscribe({
            next: user => {
              this.tokenService.setUser(user);
              this.user = user;
            }
          });
        },
        error: error => {
          console.log("error")
          this.tokenService.logOut()
        }
      })
    }
  }
  base64ToBlob(base64String: string): Promise<Blob> {
    return new Promise<Blob>((resolve, reject) => {
      const byteCharacters = atob(base64String);
      const byteArrays = [];
      for (let offset = 0; offset < byteCharacters.length; offset += 512) {
        const slice = byteCharacters.slice(offset, offset + 512);
        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
          byteNumbers[i] = slice.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
      }
      const blob = new Blob(byteArrays, { type: 'image/jpeg' });
      resolve(blob);
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
