package com.fordogs.product.presentation.request;

import com.fordogs.product.domain.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "상품 수정 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductUpdateRequest {

    @Schema(description = "상품명", example = "강아지 하우스 [SMALL]")
    private String productName;

    @Schema(description = "상품 가격", example = "200")
    private Integer productPrice;

    @Schema(description = "상품 수량", example = "99")
    private Integer productQuantity;

    @Schema(description = "상품 설명", example = "강아지 집 회색 계열입니다(재질 : 극세사)")
    private String productDescription;

    @Schema(description = "상품 이미지", example = "[\"image1.jpg\", \"image2.jpg\"]")
    private String[] productImages;

    @Schema(description = "상품 카테고리")
    @NotNull(message = "상품 카테고리를 입력해주세요.")
    private Category productCategory;
}
