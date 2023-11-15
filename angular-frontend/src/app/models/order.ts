import {Seller} from "./seller";
import {ProductWithImage} from "./product-with-image";

export class Order {
    productIdAndQuantity: {[id: string]: number};
    productsAndQuantity: Map<ProductWithImage, number>;
    orderedAt: string;
    status: string;
    isPaid: boolean;
    customer: Seller;
}
