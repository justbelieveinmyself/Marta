import { Seller } from "./seller";

export class Product {
    id!: number;
    name!: string;
    price!: number;
    count!: number;
    description!: string;
    manufacturer!: string;
    structure!: string;
    seller!: Seller;
}