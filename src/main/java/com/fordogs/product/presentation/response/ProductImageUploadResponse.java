package com.fordogs.product.presentation.response;

import com.fordogs.product.application.aws.s3.response.ImageUploadInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "상품 이미지 파일 업로드 응답")
@Getter
@Setter
@Builder
public class ProductImageUploadResponse {

    @Schema(description = "상품 이미지 파일 업로드 응답 목록")
    private List<ImageUploadInfo> uploadedImages;

    public static ProductImageUploadResponse toResponse(List<ImageUploadInfo> uploadedImages) {
        return ProductImageUploadResponse.builder()
                .uploadedImages(uploadedImages)
                .build();
    }
}
