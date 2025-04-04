package com.green.ca2sa.cafe.category.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeCategoryGetRes {
    @Schema(title = "카테고리 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long categoryId;
    @Schema(title = "카테고리 이름", example = "커피", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryName;
    @JsonIgnore
    private long cafeId;
}
