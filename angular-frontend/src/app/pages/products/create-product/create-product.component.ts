import {Component, OnInit} from '@angular/core';
import {Product} from '../../../models/product';
import {ProductService} from '../../../services/product.service';
import {Router} from '@angular/router';
import {TokenService} from 'src/app/services/token.service';
import {LocalUser} from 'src/app/models/local-user';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-create-product',
    templateUrl: './create-product.component.html',
    styleUrls: ['./create-product.component.css']
})
export class CreateProductComponent implements OnInit {
    product: Product = new Product();
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
            count: [null, [Validators.required, Validators.min(3)]],
            description: ['', Validators.required],
            structure: ['' ],
            manufacturer: ['', [Validators.required, Validators.nullValidator]],
            category: [null, Validators.required]
        });
    }

    saveProduct() {
        this.product.productName = this.productForm.value.productName;
        this.product.price = this.productForm.value.price;
        this.product.count = this.productForm.value.count;
        this.product.description = this.productForm.value.description;
        this.product.structure = this.productForm.value.structure;
        this.product.manufacturer = this.productForm.value.manufacturer;
        this.product.category = this.productForm.value.category;
        if(this.productForm.invalid){
            return;
        }
        this.product.seller = this.user;
        this.productService.addProduct(this.product, this.previewImg).subscribe(data => {
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
