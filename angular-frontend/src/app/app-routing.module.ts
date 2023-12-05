import {NgModule} from '@angular/core';
import {ExtraOptions, RouterModule, Routes} from '@angular/router';
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
import {SearchComponent} from "./search/search.component";

const routes: Routes = [
    {path: 'products', component: MainPageComponent},
    {path: 'adminPanel', component: AdminPageComponent, canActivate: [AdminGuard]},
    {path: 'adminPanel/activity/:id', component: ActivityPageComponent, canActivate: [AdminGuard]},
    {path: 'products/:id/details', component: ProductDetailsComponent},
    {path: 'products/:id/feedback', component: ProductFeedbackComponent},
    {path: 'products/:id/questions', component: ProductQuestionsComponent},
    {path: 'products/search', component: SearchComponent},
    {path: '', redirectTo: 'products', pathMatch: 'full'},
    {path: 'create-product', component: CreateProductComponent},
    {path: 'update-product/:id', component: UpdateProductComponent},
    {path: 'login', component: LoginComponent},
    {path: 'register', component: RegisterComponent},
    {path: 'profile/cart', component: ProductCartComponent},
    {path: 'profile', component: UserProfileComponent},
    {path: 'profile/details', component: UserDetailsComponent},
    {path: 'profile/favourites', component: UserFavouritesComponent},
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
