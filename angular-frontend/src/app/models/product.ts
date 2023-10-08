import { Seller } from "./seller";

export class Product {
    id!: number;
    productCode!: string;
    productName!: string;
    price!: number;
    count!: number;
    description!: string;
    manufacturer!: string;
    structure!: string;
    seller!: Seller;
}