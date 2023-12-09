import {Component, Input} from '@angular/core';
import {ProductWithImage} from 'src/app/models/product-with-image';
import {Order} from "../../models/order";
import {ProductService} from "../../service/product.service";
import {Product} from "../../models/product";
import {TokenService} from "../../service/token.service";
import {LocalUser} from "../../models/local-user";
import {UserService} from "../../service/user.service";

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html',
    styleUrls: ['./product-list.component.css']
})
export class ProductListComponent{
    constructor(
        private productService: ProductService,
        private tokenService: TokenService,
        private userService: UserService
    ) {}

    @Input() card: ProductWithImage[];
    @Input() orders: Order[];
    @Input() isOrders: boolean;
    @Input() isAdminPage: boolean;
    products: ProductWithImage[];
    productInToast: Product;
    currentUser: LocalUser;
    isFavourite: boolean[];
    favourites: ProductWithImage[];
    ngOnInit() {
        if(!this.card && !this.orders){
            setTimeout(() => this.ngOnInit(), 50);
            console.log("test");
        }
        if(!this.favourites){
            let index = 0;
            this.userService.getFavourites().subscribe({
                next: favourites => {
                    favourites.forEach(favour => {
                        // this.isFavourite[]
                        index++;
                    })
                }
            })
        }
        this.products = this.card;
        this.currentUser = this.tokenService.getUser();
    }

    addToCart(product: Product){
        this.productService.addProductToCart(product).subscribe({
            error: err => console.log(err)
        });
        this.productInToast = product;
        console.log(this.productInToast)
        // @ts-ignore
        document.getElementById('liveToast').classList.add("show");
    }

    verifyProduct(product: Product, index: number){
        this.productService.verifyProduct(product.id).subscribe({
            next: res => {
                this.card.at(index).product.isVerified = true;
            }
        })
    }

    protected readonly Array = Array;

    deleteProduct(product: Product, index: number) {
        this.productService.deleteProduct(product.id).subscribe({
            next: res => {
                this.card.splice(index, 1);
            }
        })
    }

    addOrRemoveFavourite(item: ProductWithImage, isFavourite: boolean) {
        if(isFavourite){
            this.productService.deleteProductFromFavourite(item.product).subscribe({
                // next: next => item.product = next,
                error: err => console.log(err)
            })
        }else {
            this.productService.addProductToFavourite(item.product).subscribe({
                next: next => item.product = next,
                error: err => console.log(err)
            });
        }
    }

}
