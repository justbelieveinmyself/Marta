import {Component, OnInit} from '@angular/core';
import {Product} from '../../../models/product';
import {ProductService} from '../../../services/product.service';
import {Router} from '@angular/router';
import {TokenService} from 'src/app/services/token.service';
import {LocalUser} from 'src/app/models/local-user';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ProductDetail} from "../../../models/product-detail";
import {Seller} from "../../../models/seller";

@Component({
    selector: 'app-create-product',
    templateUrl: './create-product.component.html',
    styleUrls: ['./create-product.component.css']
})
export class CreateProductComponent implements OnInit {
    product: Product = new Product();
    productDetail: ProductDetail = new ProductDetail();
    user: LocalUser;
    previewImg: File;
    productForm: FormGroup;
    categories: string[] =
        ["Women's clothing", "Men's clothing", "Children's clothing", "Outerwear",
            "Footwear", "Sporting goods", "Accessories", "Cosmetics and perfumes",
            "Household Goods", "Electronics", "Children's goods and toys",
            "Beauty and health products", "Bags and accessories", "Watch", "Jewelry",
            "Gourmet products", "Books and audiobooks", "Sports nutrition",
            "Automotive products", "Furniture and household goods",
            "Tools and automotive supplies", "Pet Supplies"
        ];
    constructor(
        private productService: ProductService,
        private router: Router,
        private tokenService: TokenService,
        private formBuilder: FormBuilder
    ) {}

    ngOnInit() {
        this.user = this.tokenService.getUser();
        this.productForm = this.formBuilder.group({
            productName: ['', [Validators.required, Validators.nullValidator]],
            price: [null, [Validators.required, Validators.min(1)]],
            category: [null, Validators.required],
            discountPercentage: [null, [Validators.required]],
            description: ['', Validators.required],
            structure: [''],
            manufacturer: ['', [Validators.required, Validators.nullValidator]],
            dimensions: [null, [Validators.required]],
            weight: [null, [Validators.required]],
            otherDetails: [null],
            color: [null, [Validators.required]],
            material: [null]
        });
    }

    saveProduct() {
        this.product.productName = this.productForm.value.productName;
        this.product.price = this.productForm.value.price;
        this.product.category = this.productForm.value.category;
        this.product.discountPercentage = this.productForm.value.discountPercentage;
        this.productDetail.description = this.productForm.value.description;
        this.productDetail.structure = this.productForm.value.structure;
        this.productDetail.manufacturer = this.productForm.value.manufacturer;
        this.productDetail.dimensions = this.productForm.value.dimensions;
        this.productDetail.weight = this.productForm.value.weight;
        this.productDetail.otherDetails = this.productForm.value.otherDetails;
        this.productDetail.color = this.productForm.value.color;
        this.productDetail.material = this.productForm.value.material;
        this.product.isVerified = false;

        if(this.productForm.invalid){
            return;
        }
        this.product.seller = this.user;
        this.productService.addProduct(this.product, this.previewImg, this.productDetail).subscribe(data => {
            console.log(data);
            this.redirectToProductList();
        });
    }

    redirectToProductList() {
        this.router.navigate(['/products']);
    }

    onFileSelected(event: any) {
        this.previewImg = event.target.files[0];
    }

}
