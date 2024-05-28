package com.fordogs.product.application.aws.s3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "S3 이미지 업로드 응답")
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageUploadResponse {

    @Schema(description = "이미지 파일 이름")
    private String imageFilename;

    @Schema(description = "이미지 파일 URL")
    private String imageFileUrl;

    public static ImageUploadResponse toResponse(String imageFilename, String imageFileUrl) {
        return ImageUploadResponse.builder()
                .imageFilename(imageFilename)
                .imageFileUrl(imageFileUrl)
                .build();
    }
}
