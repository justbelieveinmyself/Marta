import { Component } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../service/product.service';
import { Router } from '@angular/router';
import { TokenService } from 'src/app/service/token.service';
import { LocalUser } from 'src/app/models/local-user';
import { ProductWithImage } from 'src/app/models/product-with-image';
import { ImageService } from 'src/app/service/image.service';

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
    private imageService: ImageService){}
  
  ngOnInit(): void {
    this.user = this.tokenService.getUser();
    this.getProducts();
  }

  private getProducts(){
    this.productService.getProductList().subscribe({
      next: data =>{
      this.card = data;
      console.log(data);
  
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
      console.log(this.card)
    },
    error: e => {
      if(e.status == 403){
        this.router.navigate(['/login']);
      }
    }
    });
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
