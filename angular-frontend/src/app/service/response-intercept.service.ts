import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, tap} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class ResponseInterceptService implements HttpInterceptor {

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(
            tap(event => {
                console.log("xd")
                console.log(event.type)
                setTimeout(() => {
                    if (event.type === 4) {
                        console.log('Response from server:', event.type);
                    }else{
                        console.log('nullable')
                        console.log('Response from server:', event.type);
                    }
                }, 4000);

            })
        );
    }
}
export const responseInterceptorProvider = [{
    provide: HTTP_INTERCEPTORS,
    useClass: ResponseInterceptService,
    multi: true
}]
