import { Seller } from "./seller";

export class Product {
    id!: number;
    productName!: string;
    productCode!: string;
    category!: string;
    price!: number;
    count!: number;
    description!: string;
    manufacturer!: string;
    structure!: string;
    seller!: Seller;
}
