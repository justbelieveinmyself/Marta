import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from "@angular/router";
import {map, Observable} from "rxjs";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{

    constructor(private activatedRoute: ActivatedRoute) {}

    search: Observable<string>;

    ngOnInit(): void {
        this.search = this.activatedRoute.queryParamMap.pipe(map((params: ParamMap) => params.get('keyword')));
    }

}
