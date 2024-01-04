import {Component} from '@angular/core';
import {NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
    loading = false;

    constructor(private router: Router) {
        this.router.events.subscribe(ev => {
            if(ev instanceof NavigationStart) {
                this.loading = true;
                console.log("Start");
            }
            if(ev instanceof NavigationEnd || ev instanceof NavigationCancel || ev instanceof NavigationError) {
                this.loading = false;
                console.log("End");
            }
        })
    }
}
