import {inject, NgModule} from '@angular/core';
import {ActivatedRouteSnapshot, ExtraOptions, RouterModule, Routes} from '@angular/router';
import {CreateProductComponent} from './products/create-product/create-product.component';
import {UpdateProductComponent} from './products/update-product/update-product.component';
import {LoginComponent} from './auth/login/login.component';
import {RegisterComponent} from './auth/register/register.component';
import {UserDetailsComponent} from './user-details/user-details.component';
import {UserProfileComponent} from './user-profile/user-profile.component';
import {UserFavouritesComponent} from './user-favourites/user-favourites.component';
import {UserDeliveryComponent} from './user-delivery/user-delivery.component';
import {ProductDetailsComponent} from "./products/product-details/product-details.component";
import {ProductFeedbackComponent} from "./products/product-feedback/product-feedback.component";
import {ProductQuestionsComponent} from "./products/product-questions/product-questions.component";
import {ProductCartComponent} from "./products/product-cart/product-cart.component";
import {MainPageComponent} from "./main-page/main-page.component";
import {AdminPageComponent} from "./admin-page/admin-page.component";
import {AdminGuard} from "./admin-guard";
import {ActivityPageComponent} from "./activity-page/activity-page.component";
import {ProductService} from "./service/product.service";
import {UserService} from "./service/user.service";

const routes: Routes = [
    {
        path: 'products', component: MainPageComponent,
        resolve: {
            productsPage: (route: ActivatedRouteSnapshot) => {
                const page = route.queryParams['page'] || 0;
                const size = route.queryParams['size'] || 12;
                const sortBy = route.queryParams['sortBy'];
                const isAsc = route.queryParams['isAsc'] === 'true';
                const isFilteredByWithPhoto = route.queryParams['isFilteredByWithPhoto'] === 'true';
                const isFilteredByVerified = route.queryParams['isFilteredByVerified'] === 'true';
                const searchWord = route.queryParams['searchWord'];
                return inject(ProductService).getProductList(page, size, true, sortBy, isAsc, isFilteredByWithPhoto, isFilteredByVerified, searchWord);
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
    {
        path: 'products/search', component: MainPageComponent,
        resolve: {
            productsPage: (route: ActivatedRouteSnapshot) => {
                const page = route.queryParams['page'] || 0;
                const size = route.queryParams['size'] || 12;
                const sortBy = route.queryParams['sortBy'];
                const isAsc = route.queryParams['isAsc'] === 'true';
                const isFilteredByWithPhoto = route.queryParams['isFilteredByWithPhoto'] === 'true';
                const isFilteredByVerified = route.queryParams['isFilteredByVerified'] === 'true';
                const searchWord = route.queryParams['searchWord'];
                return inject(ProductService).getProductList(page, size, true, sortBy, isAsc, isFilteredByWithPhoto, isFilteredByVerified, searchWord);
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
    {path: 'profile/cart', component: ProductCartComponent},
    {path: 'profile', component: UserProfileComponent},
    {path: 'profile/details', component: UserDetailsComponent},
    {path: 'profile/favourites', component: UserFavouritesComponent,
        resolve: {
            favouritesProducts: (route: ActivatedRouteSnapshot) => {
                return inject(UserService).getFavourites();
            }
        }
    },
    {path: 'profile/delivery', component: UserDeliveryComponent},
    {path: '**', redirectTo: 'create-product', pathMatch: 'full'}

];
const routerOptions: ExtraOptions = {
    anchorScrolling: 'enabled',
    onSameUrlNavigation: 'reload'
};

@NgModule({
    imports: [RouterModule.forRoot(routes, routerOptions)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
