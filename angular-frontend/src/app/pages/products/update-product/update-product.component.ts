import {Component} from '@angular/core';
import {Product} from '../../../models/product';
import {ProductService} from '../../../services/product.service';
import {ActivatedRoute, Router} from '@angular/router';
import {LocalUser} from 'src/app/models/local-user';
import {TokenService} from 'src/app/services/token.service';

@Component({
    selector: 'app-update-product',
    templateUrl: './update-product.component.html',
    styleUrls: ['./update-product.component.css']
})
export class UpdateProductComponent {
    user: LocalUser;
    product: Product;
    hasRights: boolean;

    constructor(
        private productService: ProductService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private tokenService: TokenService
    ) {}

    ngOnInit() {
        this.user = this.tokenService.getUser();
        this.activatedRoute.data.subscribe(data => {
            this.product = data["product"].product;
            this.hasRights = this.user.id == this.product.seller.id;
        });
    }

    onSubmit() {
        this.product.seller = this.user;
        this.productService.updateProduct(this.product).subscribe(data => {
            console.log(data);
            this.redirectToProductList();
        });

    }

    redirectToProductList() {
        this.router.navigate(['products']);
    }
}
