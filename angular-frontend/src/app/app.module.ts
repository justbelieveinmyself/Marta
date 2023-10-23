import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ProductListComponent} from './products/product-list/product-list.component';
import {HttpClientModule} from '@angular/common/http';
import {CreateProductComponent} from './products/create-product/create-product.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UpdateProductComponent} from './products/update-product/update-product.component';
import {LoginComponent} from './auth/login/login.component'
import {SocialAuthServiceConfig, SocialLoginModule, VKLoginProvider} from '@abacritt/angularx-social-login';
import {RegisterComponent} from './auth/register/register.component';
import {NavbarComponent} from './navbar/navbar.component';
import {interceptorProvider} from './service/prod-intercept.service';
import {UserProfileComponent} from './user-profile/user-profile.component';
import {UserDetailsComponent} from './user-details/user-details.component';
import {UserDeliveryComponent} from './user-delivery/user-delivery.component';
import {UserFavouritesComponent} from './user-favourites/user-favourites.component';
import {LogoutComponent} from './auth/logout/logout.component';
import {ProductDetailsComponent} from './products/product-details/product-details.component';
import {PopoverModule} from "ngx-bootstrap/popover";
import {ProductFeedbackComponent} from './products/product-feedback/product-feedback.component';
import {NgOptimizedImage} from "@angular/common";

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
    ProductFeedbackComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        SocialLoginModule,
        PopoverModule.forRoot(),
        NgOptimizedImage
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
    [interceptorProvider]
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
