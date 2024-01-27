import {Component, Input} from '@angular/core';
import {ProductWithImage} from 'src/app/models/product-with-image';
import {Order} from "../../../models/order";
import {ProductService} from "../../../services/product.service";
import {Product} from "../../../models/product";
import {TokenService} from "../../../services/token.service";
import {LocalUser} from "../../../models/local-user";
import {UserService} from "../../../services/user.service";
import {Seller} from "../../../models/seller";
import {ProductInteractionService} from "../../../services/product-interaction.service";

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html',
    styleUrls: ['./product-list.component.css']
})
export class ProductListComponent {
    constructor(
        private productService: ProductService,
        private tokenService: TokenService,
        private userService: UserService,
        private productInteractionService: ProductInteractionService
    ) {}

    @Input() card: ProductWithImage[];
    @Input() orders: Order[];
    @Input() isOrders: boolean;
    @Input() isAdminPage: boolean;
    @Input() isNeedFavourites: boolean;
    @Input() isCanEdit: boolean;
    @Input() isItemsByGrid: boolean = true;
    productInToast: Product;
    contactSeller: Seller = new Seller();
    currentUser: LocalUser;
    messageForSeller = "";
    isFavourite: boolean[];

    ngOnInit() {
        this.currentUser = this.tokenService.getUser();
    }
    ngOnChanges(){
        if(this.isNeedFavourites && this.card){
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
            }
        })
    }

    addToCart(product: Product){
        this.productService.addProductToCart(product).subscribe({
            error: err => console.log(err)
        });
        this.productInToast = product;
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

    toggleFavourite(item: ProductWithImage, index: number) {
        const isFavourite = this.isFavourite[index];

        this.productInteractionService.toggleFavourite(isFavourite, item.product.id).subscribe(isFavourite => {
           this.isFavourite[index] = isFavourite;
        });
    }


    sendMessageToSeller() {

    }
}
