import {Product} from "./product";

export class LocalUser {
    id!: number;
    firstName!: string;
    lastName!: string;
    username!: string;
    email!: string;
    age!: number;
    gender!: string;
    roles!: string[];
    avatar!: string;
    phone!: string;
    address!: string;
    city!: string;
    postalCode!: string;
    country!: string;
    balance!: number;
    favouriteProducts!: Product[];
}
