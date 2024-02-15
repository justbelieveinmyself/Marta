import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ProductListComponent} from './pages/products/product-list/product-list.component';
import {HttpClientModule} from '@angular/common/http';
import {CreateProductComponent} from './pages/products/create-product/create-product.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UpdateProductComponent} from './pages/products/update-product/update-product.component';
import {LoginComponent} from './pages/authentication/login/login.component'
import {SocialAuthServiceConfig, SocialLoginModule, VKLoginProvider} from '@abacritt/angularx-social-login';
import {RegisterComponent} from './pages/authentication/register/register.component';
import {NavbarComponent} from './components/navbar/navbar.component';
import {requestInterceptorProvider} from './services/request-intercept.service';
import {UserProfileComponent} from './pages/users/user-profile/user-profile.component';
import {UserDetailsComponent} from './pages/users/user-details/user-details.component';
import {UserDeliveryComponent} from './pages/users/user-delivery/user-delivery.component';
import {UserFavouritesComponent} from './pages/users/user-favourites/user-favourites.component';
import {LogoutComponent} from './pages/authentication/logout/logout.component';
import {ProductDetailsComponent} from './pages/products/product-details/product-details.component';
import {PopoverModule} from "ngx-bootstrap/popover";
import {ProductFeedbackComponent} from './pages/products/product-feedback/product-feedback.component';
import {NgOptimizedImage} from "@angular/common";
import {ProductQuestionsComponent} from './pages/products/product-questions/product-questions.component';
import {ProductCartComponent} from './pages/products/product-cart/product-cart.component';
import {MainPageComponent} from './pages/main-page/main-page.component';
import {AdminPageComponent} from './pages/users/admin-page/admin-page.component';
import {errorInterceptorProvider} from "./services/error-intercept.service";
import { ActivityPageComponent } from './pages/users/activity-page/activity-page.component';
import {TooltipModule} from "ngx-bootstrap/tooltip";
import { SearchComponent } from './components/search/search.component';
import { PreloaderComponent } from './components/preloader/preloader.component';
import { SellerPageComponent } from './pages/seller-page/seller-page.component';
import { OrderListComponent } from './pages/products/order-list/order-list.component';
import {NgxImageZoomModule} from "ngx-image-zoom";

@NgModule({
  declarations: [
    AppComponent,
    ProductListComponent,
    CreateProductComponent,
    UpdateProductComponent,
    LoginComponent,
    RegisterComponent,
    NavbarComponent,
    UserProfileComponent,
    UserDetailsComponent,
    UserDeliveryComponent,
    UserFavouritesComponent,
    LogoutComponent,
    ProductDetailsComponent,
    ProductFeedbackComponent,
    ProductQuestionsComponent,
    ProductCartComponent,
    MainPageComponent,
    AdminPageComponent,
    ActivityPageComponent,
    SearchComponent,
    PreloaderComponent,
    SellerPageComponent,
    OrderListComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        SocialLoginModule,
        PopoverModule.forRoot(),
        NgOptimizedImage,
        TooltipModule,
        NgxImageZoomModule
    ],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: VKLoginProvider.PROVIDER_ID,
            provider: new VKLoginProvider(
              '51757108'
            )
          }
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    },
    [requestInterceptorProvider, errorInterceptorProvider]
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
