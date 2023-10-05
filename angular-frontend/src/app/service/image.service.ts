import { Injectable, SecurityContext } from '@angular/core';
import { UserService } from './user.service';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(
    private userService : UserService,
    private sanitizer : DomSanitizer
  ) { }
  getUserAvatar(userId : number) : Promise<string>{
    return new Promise<string>((resolve, reject) => {
      let result = '';
  
      if (localStorage.getItem("avatar") == null) {
        this.userService.getAvatar(userId).subscribe(blob => {
          this.blobToBase64(blob).then(base64String => {
            localStorage.setItem("avatar", base64String);
            result = URL.createObjectURL(blob);
            resolve(this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, this.sanitizer.bypassSecurityTrustResourceUrl(result))!);
          }).catch(error => {
            reject(error);
          })
        }, error => reject(error));
  
      } else {
        const base64String = localStorage.getItem("avatar")!;
  
        this.base64ToBlob(base64String).then(blob => {
          result = URL.createObjectURL(blob);
          resolve(this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, this.sanitizer.bypassSecurityTrustResourceUrl(result))!);
        }).catch(error => {
          reject(error);
        });
      }
    });
  }
  blobToBase64(blob : Blob) : Promise<string> {
    return new Promise( (resolve, reject) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result as string);
      reader.onerror = reject;
      reader.readAsDataURL(blob);
    });
  }

  base64ToBlob(base64String : string) : Promise<Blob>{
    return fetch(base64String).then(response => response.blob());
  }
}
