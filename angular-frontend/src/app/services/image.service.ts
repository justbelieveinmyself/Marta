import {Injectable, SecurityContext} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';
import {ProductWithImage} from "../models/product-with-image";
import {LocalUser} from "../models/local-user";
import {ProductDetail} from "../models/product-detail";

@Injectable({
    providedIn: 'root'
})
export class ImageService {
    constructor(
        private sanitizer: DomSanitizer
    ) {}

    createUrlFromBlob(blob: Blob) {
        let result = URL.createObjectURL(blob);
        return this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, this.sanitizer.bypassSecurityTrustResourceUrl(result))!;
    }

    createUrlFromBlobProm(blob: Blob): Promise<string> {
        return new Promise<string>((resolve, reject) => {
            let result = URL.createObjectURL(blob);
            resolve(this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, this.sanitizer.bypassSecurityTrustResourceUrl(result))!);
        })
    }

    blobToBase64(blob: Blob): Promise<string> {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onloadend = () => resolve(reader.result as string);
            reader.onerror = reject;
            reader.readAsDataURL(blob);
        });
    }

    base64ToBlobFromUrl(base64String: string): Promise<Blob> {
        return fetch(base64String).then(response => response.blob());
    }

    base64ToBlob(base64String: string): Promise<Blob> {
        return new Promise<Blob>((resolve, reject) => {
            const byteCharacters = atob(base64String);
            const byteArrays = [];
            for (let offset = 0; offset < byteCharacters.length; offset += 512) {
                const slice = byteCharacters.slice(offset, offset + 512);
                const byteNumbers = new Array(slice.length);
                for (let i = 0; i < slice.length; i++) {
                    byteNumbers[i] = slice.charCodeAt(i);
                }
                const byteArray = new Uint8Array(byteNumbers);
                byteArrays.push(byteArray);
            }
            const blob = new Blob(byteArrays, {type: 'image/jpeg'});
            resolve(blob);
        });
    }

    createImageInProduct(product: ProductWithImage): ProductWithImage {
        if (product.file == "") {
            product.file = "https://www.webstoresl.com/sellercenter/assets/images/no-product-image.png";
            return product;
        }
        let image;
        this.base64ToBlob(product.file).then(blob => {
            image = this.createUrlFromBlobProm(blob);
            return image;
        }).then(url => {
            product.file = url;
        });
        return product;
    }

    createAvatarInUser(user: LocalUser): LocalUser {
        if (!user.avatar) {
            user.avatar = "https://www.webstoresl.com/sellercenter/assets/images/no-product-image.png";
            return user;
        }
        let image;
        this.base64ToBlob(user.avatar).then(blob => {
            image = this.createUrlFromBlobProm(blob);
            return image;
        }).then(url => {
            user.avatar = url;
        });
        return user;
    }

    createUrlFromBase64(base64String: string) {
        return new Promise<string>(resolve => {
            this.base64ToBlob(base64String).then(blob => {
                resolve(this.createUrlFromBlob(blob));
            })
        })
    }
}
