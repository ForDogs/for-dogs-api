package com.fordogs.product.error;

import com.fordogs.core.exception.error.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements BaseErrorCode<S3Exception> {

    IMAGE_FILE_EMPTY(HttpStatus.BAD_REQUEST, "이미지 파일이 비어 있습니다."),
    INVALID_IMAGE_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않는 이미지 파일 확장자입니다. 허용되는 확장자는 'jpg', 'jpeg', 'png', 'gif'입니다."),
    NO_IMAGE_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "이미지 파일의 확장자를 찾을 수 없습니다."),
    INVALID_IMAGE_URL(HttpStatus.BAD_REQUEST, "이미지 URL에서 키를 추출하는 도중 오류가 발생했습니다. 올바른 이미지 URL을 입력하세요."),
    S3_BUCKET_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 버킷에 이미지 파일을 업로드하는 도중 오류가 발생했습니다."),
    S3_BUCKET_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 버킷에서 이미지 파일을 삭제하는 도중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public S3Exception toException() {
        return (S3Exception) S3Exception.builder().message(message).statusCode(httpStatus.value()).build();
    }
}
