import {inject, NgModule} from '@angular/core';
import {ActivatedRouteSnapshot, RouterModule, Routes} from '@angular/router';
import {CreateProductComponent} from './pages/products/create-product/create-product.component';
import {UpdateProductComponent} from './pages/products/update-product/update-product.component';
import {LoginComponent} from './pages/authentication/login/login.component';
import {RegisterComponent} from './pages/authentication/register/register.component';
import {UserDetailsComponent} from './pages/users/user-details/user-details.component';
import {UserProfileComponent} from './pages/users/user-profile/user-profile.component';
import {UserFavouritesComponent} from './pages/users/user-favourites/user-favourites.component';
import {UserDeliveryComponent} from './pages/users/user-delivery/user-delivery.component';
import {ProductDetailsComponent} from "./pages/products/product-details/product-details.component";
import {ProductFeedbackComponent} from "./pages/products/product-feedback/product-feedback.component";
import {ProductQuestionsComponent} from "./pages/products/product-questions/product-questions.component";
import {ProductCartComponent} from "./pages/products/product-cart/product-cart.component";
import {MainPageComponent} from "./pages/main-page/main-page.component";
import {AdminPageComponent} from "./pages/users/admin-page/admin-page.component";
import {AdminGuard} from "./admin-guard";
import {ActivityPageComponent} from "./pages/users/activity-page/activity-page.component";
import {ProductService} from "./services/product.service";
import {UserService} from "./services/user.service";
import {PageDataService} from "./services/page-data.service";
import {OrderService} from "./services/order.service";
import {SellerPageComponent} from "./pages/seller-page/seller-page.component";
import {TokenService} from "./services/token.service";

const routes: Routes = [
    {
        path: 'products', component: MainPageComponent,
        resolve: {
            productsPage: (route: ActivatedRouteSnapshot) => {
                const pageDataService = inject(PageDataService);
                const pageNumber = route.queryParams['page']-1 || 0;
                pageDataService.pageNumber = pageNumber > 0? pageNumber : 0;
                pageDataService.sizeOfPage = route.queryParams['size'] || 12;
                pageDataService.sortBy = route.queryParams['sortBy'];
                pageDataService.isSortASC = route.queryParams['isASC'] === 'true';
                pageDataService.isFilteredByWithPhoto = route.queryParams['onlyWithPhoto'] === 'true';
                pageDataService.isFilteredByVerified = route.queryParams['onlyVerified'] === 'true';
                pageDataService.searchWord = route.queryParams['search'];
                return inject(ProductService).getProductList(pageDataService.pageNumber, pageDataService.sizeOfPage, true, pageDataService.sortBy, pageDataService.isSortASC, pageDataService.isFilteredByWithPhoto, pageDataService.isFilteredByVerified, pageDataService.searchWord);
            }
        }
    },
    {
        path: 'adminPanel', component: AdminPageComponent, canActivate: [AdminGuard],
        resolve: {
            users: () => {
                return inject(UserService).getUsers();
            }
        }
    },
    {
        path: 'adminPanel/activity/:id', component: ActivityPageComponent, canActivate: [AdminGuard],
        resolve: {
            user: (route: ActivatedRouteSnapshot) => {
                const param = route.params["id"];
                return inject(UserService).getUserCurrentOrById(param);
            },
            products: (route: ActivatedRouteSnapshot) => {
                return inject(ProductService).getProductList(0, 1, false);
            }
        }
    },
    {
        path: 'products/:id/details', component: ProductDetailsComponent,
        resolve: {
            product: (route: ActivatedRouteSnapshot) => {
                const param = route.params["id"];
                return inject(ProductService).getProductById(param);
            },
            productDetail: (route: ActivatedRouteSnapshot) => {
                const param = route.params["id"];
                return inject(ProductService).getProductDetailById(param);
            }
        }
    },
    {
        path: 'products/:id/feedback', component: ProductFeedbackComponent,
        resolve: {
            product: (route: ActivatedRouteSnapshot) => {
                const param = route.params["id"];
                return inject(ProductService).getProductById(param);
            }
        }
    },
    {
        path: 'products/:id/questions', component: ProductQuestionsComponent,
        resolve: {
            product: (route: ActivatedRouteSnapshot) => {
                const param = route.params["id"];
                return inject(ProductService).getProductById(param);
            }
        }
    },
    {path: '', redirectTo: 'products', pathMatch: 'full'},
    {path: 'create-product', component: CreateProductComponent},
    {path: 'update-product/:id', component: UpdateProductComponent,
        resolve: {
            product: (route: ActivatedRouteSnapshot) => {
                const param = route.params["id"];
                return inject(ProductService).getProductById(param);
            }
        }
    },
    {path: 'login', component: LoginComponent},
    {path: 'register', component: RegisterComponent},
    {path: 'profile/cart', component: ProductCartComponent,
        resolve: {
            products: () => {
                return inject(ProductService).getProductsFromCart();
            }
        }
    },
    {path: 'profile', component: UserProfileComponent},
    {path: 'profile/my-products', component: SellerPageComponent,
        resolve: {
            products: () => {
                const id = inject(TokenService).getUser().id;
                return inject(UserService).getUserProducts(id);
            }
        }
    },
    {path: 'profile/details', component: UserDetailsComponent},
    {path: 'profile/favourites', component: UserFavouritesComponent,
        resolve: {
            favouritesProducts: (route: ActivatedRouteSnapshot) => {
                return inject(UserService).getFavourites();
            }
        }
    },
    {path: 'profile/delivery', component: UserDeliveryComponent,
        resolve: {
            orders: () => {
                return inject(OrderService).getCurrentUserOrders();
            }
        }
    },
    {path: '**', redirectTo: 'products', pathMatch: 'full'}

];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
