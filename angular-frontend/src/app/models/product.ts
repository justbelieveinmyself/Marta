import {Seller} from "./seller";

export class Product {
    id: number;
    productName: string;
    productCode: string;
    category: string;
    price: number;
    discountPercentage: number;
    isVerified: boolean;
    updatedAt: string;
    seller: Seller;
}
