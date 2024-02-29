package com.justbelieveinmyself.marta.domain.dto;

import lombok.Data;

@Data
public class ProductListRequest {
    private Boolean usePages = true;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "popularity";
    private Boolean isAsc = false;
    private Boolean filterPhotoNotNull = false;
    private Boolean filterVerified = false;
    private String searchWord;
}
