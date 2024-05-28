package com.fordogs.core.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements BaseErrorCode<S3Exception> {

    IMAGE_FILE_EMPTY(HttpStatus.BAD_REQUEST, "비어 있는 이미지 파일입니다."),
    INVALID_IMAGE_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않는 확장자의 이미지 파일입니다. 가능한 확장자는 'jpg', 'jpeg', 'png', 'gif' 입니다."),
    NO_IMAGE_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "이미지 파일의 확장자가 없습니다."),
    S3_BUCKET_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 버킷에 이미지 파일을 업로드하는 도중 오류가 발생하였습니다."),
    S3_BUCKET_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 버킷에서 이미지 파일을 삭제하는 도중 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public S3Exception toException() {
        return (S3Exception) S3Exception.builder().message(message).statusCode(httpStatus.value()).build();
    }
}
