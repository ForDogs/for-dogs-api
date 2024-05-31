package com.fordogs.product.application.aws.s3.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "업로드된 이미지 정보")
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageUploadInfo {

    @Schema(description = "이미지 파일 이름")
    private String imageFileName;

    @Schema(description = "이미지 파일 URL")
    private String imageFileUrl;

    public static ImageUploadInfo toResponse(String imageFilename, String imageFileUrl) {
        return ImageUploadInfo.builder()
                .imageFileName(imageFilename)
                .imageFileUrl(imageFileUrl)
                .build();
    }
}
