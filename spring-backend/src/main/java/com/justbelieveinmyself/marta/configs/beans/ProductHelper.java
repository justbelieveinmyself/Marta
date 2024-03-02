package com.justbelieveinmyself.marta.configs.beans;

import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.ProductImage;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.justbelieveinmyself.marta.exceptions.NotCreatedException;
import com.mysql.cj.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

public class ProductHelper {
    private final FileHelper fileHelper;
    private final ProductMapper productMapper;
    private final String[] sortOptions = {"id", "price", "updatedAt", "popularity"};
    public ProductHelper(FileHelper fileHelper, ProductMapper productMapper) {
        this.fileHelper = fileHelper;
        this.productMapper = productMapper;
    }

    public String validateSortOption(String sortOption) {
        String lowerCaseOption = sortOption.toLowerCase();
        for (String option : sortOptions) {
            if (option.equals(lowerCaseOption)) {
                return lowerCaseOption;
            }
        }
        throw new NotCreatedException("Invalid sort option: " + sortOption);
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
            String sortOption,
            Boolean isAsc,
            Boolean filterPhotoNotNull,
            Boolean filterVerified,
            String searchWord
    ) {
        Specification<Product> specification = Specification.where(null);
        if (sortOption != null) {
            if (sortOption.equals("popularity")) {
                specification = specification.and((root, query, criteriaBuilder) -> {
                    query.orderBy(criteriaBuilder.desc(getAvgRating(root)));
                    return query.getRestriction();
                });
            } else {
                if (isAsc) {
                    specification = specification.and((root, query, criteriaBuilder) ->
                    { query.orderBy(criteriaBuilder.asc(root.get(sortOption))); return null; });
                } else {
                    specification = specification.and((root, query, criteriaBuilder) ->
                    { query.orderBy(criteriaBuilder.desc(root.get(sortOption))); return null; });
                }
            }
        }

        if (filterPhotoNotNull) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(criteriaBuilder.size(root.get("productDetail").get("images")), 0));
        }

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

    private Path<Double> getAvgRating(Root<Product> root) {
        Join<Object, Object> reviewJoin = root.join("reviews", JoinType.LEFT);
        return reviewJoin.get("rating");
    }

}
