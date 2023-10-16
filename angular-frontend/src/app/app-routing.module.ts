import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './products/product-list/product-list.component';
import { CreateProductComponent } from './products/create-product/create-product.component';
import { UpdateProductComponent } from './products/update-product/update-product.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { UserFavouritesComponent } from './user-favourites/user-favourites.component';
import { UserDeliveryComponent } from './user-delivery/user-delivery.component';
import {ProductDetailsComponent} from "./products/product-details/product-details.component";
import {ProductFeedbackComponent} from "./products/product-feedback/product-feedback.component";

const routes: Routes = [
  {path: 'products', component: ProductListComponent},
  {path: 'products/:id/details', component: ProductDetailsComponent},
  {path: 'products/:id/feedback', component: ProductFeedbackComponent},
  {path: '', redirectTo: 'products', pathMatch: 'full'},
  {path: 'create-product', component: CreateProductComponent},
  {path: 'update-product/:id', component: UpdateProductComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'profile/:id', component: UserProfileComponent},
  {path: 'profile/:id/details', component: UserDetailsComponent},
  {path: 'profile/:id/favourites', component: UserFavouritesComponent},
  {path: 'profile/:id/delivery', component: UserDeliveryComponent},
  {path: '**', redirectTo: 'create-product', pathMatch: 'full'},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
