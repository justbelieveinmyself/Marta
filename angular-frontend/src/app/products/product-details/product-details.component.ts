import {Component, OnInit} from '@angular/core';
@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
  animations: []
})
export class ProductDetailsComponent implements OnInit{
  isReviews = true;
  isAppearing = false;
  ngOnInit(): void {
  }
  protected readonly Math = Math;
}
