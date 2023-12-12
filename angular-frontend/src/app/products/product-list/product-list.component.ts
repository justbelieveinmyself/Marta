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
    productInToast: Product;
    currentUser: LocalUser;
    isFavourite: boolean[];
    favourites: ProductWithImage[];
    ngOnInit() {
        if(!this.card && !this.orders){
            setTimeout(() => this.ngOnInit(), 50);
            console.log("test");
        }
        this.currentUser = this.tokenService.getUser();
        if(!this.favourites){
            this.getFavourites();
        }
    }
    ngOnChanges(){
        if(!this.card){
            this.getFavourites();
        }
    }

    getFavourites(){
        this.isFavourite = new Array(this.card.length)
        this.userService.getFavourites().subscribe({
            next: favourites => {
                for (let i = 0; i < this.card.length; i++) {
                    const currentProduct = this.card[i].product;
                    this.isFavourite[i] = favourites.some(favourite => {
                        return this.currentUser.id === favourite.product.seller.id && currentProduct.id === favourite.product.id;
                    });
                }
                console.log(this.isFavourite)
            }
        })
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

    addOrRemoveFavourite(item: ProductWithImage, index: number) {
        const isFavourite = this.isFavourite[index];
        if(isFavourite){
            this.productService.deleteProductFromFavourite(item.product).subscribe({
                next: next => this.isFavourite[index] = false,
                error: err => console.log(err)
            })
        }else {
            this.productService.addProductToFavourite(item.product).subscribe({
                next: next => this.isFavourite[index] = true,
                error: err => console.log(err)
            });
        }
    }

}
