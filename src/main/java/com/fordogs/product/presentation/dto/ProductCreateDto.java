package com.fordogs.product.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.enums.Category;
import com.fordogs.core.domian.vo.Description;
import com.fordogs.core.domian.vo.Price;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

public class ProductCreateDto {

    @Schema(description = "상품 등록 요청")
    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        @Schema(description = "상품명", requiredMode = Schema.RequiredMode.REQUIRED, example = "강아지 하우스 [SMALL]")
        @NotBlank(message = "상품명을 입력해주세요.")
        private String productName;

        @Schema(description = "상품 가격", requiredMode = Schema.RequiredMode.REQUIRED, example = "19.99")
        @NotNull(message = "상품 가격을 입력해주세요.")
        private BigDecimal productPrice;

        @Schema(description = "상품 수량", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
        private int productQuantity;

        @Schema(description = "상품 설명", example = "강아지 집 회색 계열입니다(재질 : 극세사)")
        private String productDescription;

        @Schema(description = "상품 이미지", example = "[\"image1.jpg\", \"image2.jpg\"]")
        private String[] productImages;

        @Schema(description = "상품 카테고리", defaultValue = "NONE")
        private Category productCategory;

        public ProductEntity toEntity(UserEntity userEntity) {
            return ProductEntity.builder()
                    .name(this.productName)
                    .price(Price.builder().value(this.productPrice).build())
                    .quantity(this.productQuantity)
                    .description(Description.builder().value(this.productDescription).build())
                    .images(this.productImages)
                    .category(this.productCategory)
                    .seller(userEntity)
                    .build();
        }
    }

    @Schema(description = "상품 등록 응답")
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        @Schema(description = "등록된 상품명")
        private String productName;

        @Schema(description = "등로된 상품 카테고리")
        private Category productCategory;

        public static Response toResponse(ProductEntity productEntity) {
            return Response.builder()
                    .productName(productEntity.getName())
                    .productCategory(productEntity.getCategory())
                    .build();
        }
    }
}
