package com.fordogs.product.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.product.application.aws.s3.dto.ImageUploadResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ProductImageFileUploadDto {

    @Schema(description = "상품 이미지 파일 업로드 응답")
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        @Schema(description = "상품 이미지 파일 업로드 응답 목록")
        private List<ImageUploadResponse> uploadedImages;

        public static Response toResponse(List<ImageUploadResponse> uploadedImages) {
            return Response.builder()
                    .uploadedImages(uploadedImages)
                    .build();
        }
    }
}
