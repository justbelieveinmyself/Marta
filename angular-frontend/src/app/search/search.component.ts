import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from "@angular/router";
import {map} from "rxjs";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
    constructor(private activatedRoute: ActivatedRoute) {}
    pageNumber: number;
    sizeOfPage: number;
    isSortASC: boolean;
    isFilteredByWithPhoto: boolean;
    isFilteredByVerified: boolean;
    sortBy: string;
    searchWord: string;
    ngOnInit(){
        this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get('search'))).subscribe({
            next: param => {
                this.searchWord = param
            }
        });
    }
}
