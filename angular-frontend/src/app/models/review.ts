import {Seller} from "./seller";

export class Review {
    id: number;
    message: string;
    answer: string;
    rating: number;
    photos: string[];
    time: string;
    productId: number;
    author: Seller;
}
