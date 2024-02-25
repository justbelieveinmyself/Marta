import {Component, Input} from '@angular/core';
import {ProductWithImage} from 'src/app/models/product-with-image';
import {ProductService} from "../../../services/product.service";
import {Product} from "../../../models/product";
import {TokenService} from "../../../services/token.service";
import {LocalUser} from "../../../models/local-user";
import {UserService} from "../../../services/user.service";
import {ProductInteractionService} from "../../../services/product-interaction.service";
import {Carousel} from "../../../models/carousel";

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
    @Input() isAdminPage: boolean;
    @Input() isNeedFavourites: boolean;
    @Input() isCanEdit: boolean;
    @Input() isItemsBig: boolean = true;
    productInToast: Product;
    productInPopup: ProductWithImage;
    currentUser: LocalUser;
    isFavourite: boolean[];
    shownQuickShowPopup: boolean = false;
    shownVerifiedPopup: boolean = false;
    carousel: Carousel<ProductWithImage>;
    ngOnInit() {
        this.currentUser = this.tokenService.getUser();
    }

    ngOnChanges(){
        if(this.isNeedFavourites && this.card){
            this.getFavourites();
            this.carousel = new Carousel<ProductWithImage>(this.card);
        }
    }

    getFavourites(){
        this.isFavourite = new Array(this.card.length)
        this.userService.getFavourites().subscribe({
            next: favourites => {
                for (let i = 0; i < this.card.length; i++) {
                    const currentProduct = this.card[i].product;
                    this.isFavourite[i] = favourites.some(favourite => {
                        return currentProduct.id === favourite.product.id;
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

    toggleOverflowBody() {
        if (this.shownQuickShowPopup) {
            document.body.classList.add("body--overflow")
        } else {
            document.body.classList.remove("body--overflow")
        }
    }

    orderNow() {
        this.productInteractionService.orderNow(this.productInPopup, 1, false).then(isOrdered => {
        });
    }

    copyToClipboard(text: string) {
        navigator.clipboard.writeText(text);
    }
}
