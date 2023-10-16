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
  isNeedLeftButton = false;
  ngOnInit(): void {
  }
  protected readonly Math = Math;
  rightScroll(){
    // @ts-ignore
    scrollReviews.scrollLeft += 440;
    // @ts-ignore
    this.isNeedLeftButton = scrollReviews.scrollLeft + 440 > 0;
  }
  leftScroll(){
    let elementById = document.getElementById("scrollReviews");
    // @ts-ignore
    elementById.scrollLeft -= 440;
    // @ts-ignore
    this.isNeedLeftButton = scrollReviews.scrollLeft - 440 > 0;
  }

}
