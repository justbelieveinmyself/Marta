package com.justbelieveinmyself.marta.configs.beans;

import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.ProductImage;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.mysql.cj.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

public class ProductHelper {
    private final FileHelper fileHelper;
    private final ProductMapper productMapper;

    public ProductHelper(FileHelper fileHelper, ProductMapper productMapper) {
        this.fileHelper = fileHelper;
        this.productMapper = productMapper;
    }

    public List<ProductWithImageDto> createListOfProductsWithImageOfStream(Stream<Product> stream) {
        return stream
                .map(pro -> {
                            String imageBase64 = null;
                            if (pro.getProductDetail() != null) {
                                List<ProductImage> images = pro.getProductDetail().getImages();
                                if (!images.isEmpty()) {
                                    ProductImage firstImage = images.get(0);
                                    imageBase64 = Base64.getEncoder().encodeToString(
                                            fileHelper.downloadFileAsByteArray(firstImage.getPath(), UploadDirectory.PRODUCTS)
                                    );
                                }
                            }
                            return new ProductWithImageDto(productMapper.modelToDto(pro), imageBase64);
                        }
                )
                .toList();
    }

    public Specification<Product> createSpecification(
            String sortBy,
            Boolean isAsc,
            Boolean filterPhotoNotNull,
            Boolean filterVerified,
            String searchWord
    ) {
        Specification<Product> specification = Specification.where(null);
        if (filterPhotoNotNull) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(criteriaBuilder.size(root.get("productDetail").get("images")), 0));
        }

//        if (filterPopularity) {
//            specification = specification.and((root, query, criteriaBuilder) -> {
//                Subquery<Double> subquery = query.subquery(Double.class);
//                Root<Review> reviewRoot = subquery.from(Review.class);
//                subquery.select(criteriaBuilder.avg(reviewRoot.get("rating")))
//                        .where(criteriaBuilder.equal(reviewRoot.get("product"), root));
//
//                return criteriaBuilder.greaterThanOrEqualTo(subquery.getSelection().as(Double.class), 4.5);
//            });
//        }

        if (filterVerified) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.isTrue(root.get("isVerified")));
        }

        if (!StringUtils.isEmptyOrWhitespaceOnly(searchWord)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("productName")),
                                    "%" + searchWord.toLowerCase() + "%"
                            ),
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("seller").get("username")),
                                    "%" + searchWord.toLowerCase() + "%"
                            )
                    )
            );
        }
        return specification;
    }

}
