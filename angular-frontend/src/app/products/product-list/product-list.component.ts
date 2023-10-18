import {Component} from '@angular/core';
import {ProductService} from '../../service/product.service';
import {Router} from '@angular/router';
import {TokenService} from 'src/app/service/token.service';
import {LocalUser} from 'src/app/models/local-user';
import {ProductWithImage} from 'src/app/models/product-with-image';
import {ImageService} from 'src/app/service/image.service';
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
        private productService: ProductService,
        private router: Router,
        private tokenService: TokenService,
        private imageService: ImageService,
        private authService: AuthService,
        private userService: UserService) {
    }

    ngOnInit(): void {
        try {
            this.user = this.tokenService.getUser();
            this.getProducts();
        } catch {
            this.updateAccess();
        }

    }

    private getProducts() {
        this.productService.getProductList().subscribe({
            next: data => {
                this.card = data;
            },
            error: e => {
                console.log("1");
                this.updateAccess();
            }
        });
    }

    updateAccess() {
        if (this.tokenService.getRefreshToken() != null) {
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
                    this.router.navigate(['/login']);
                }
            })
        }
    }

    updateProduct(id: number) {
        this.router.navigate(['update-product/', id]);
    }

    deleteProduct(id: number) {
        this.productService.deleteProduct(id).subscribe(data => {
            this.getProducts();
        });
    }
}
