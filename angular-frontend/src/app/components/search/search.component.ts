import {Component} from '@angular/core';
import {PageDataService} from "../../services/page-data.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
    constructor(
        public pageDataService: PageDataService
    ) {}
    searchWord: string;

}
