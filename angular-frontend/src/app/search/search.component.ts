import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from "@angular/router";
import {map} from "rxjs";
import {PageDataService} from "../service/page-data.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
    constructor(
        private activatedRoute: ActivatedRoute,
        public pageDataService: PageDataService
    ) {}
    searchWord: string;

}
