package com.fordogs.product.application.aws.s3;

import com.fordogs.configuraion.properties.AWSProperties;
import com.fordogs.product.error.S3ErrorCode;
import com.fordogs.product.application.aws.s3.response.ImageUploadInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3ImageUploader {

    private static final List<String> ALLOWED_FILE_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");

    private final AWSProperties AWSProperties;
    private final S3Client s3Client;

    public ImageUploadInfo uploadImage(MultipartFile imageFile) {
        if (imageFile.isEmpty() || Objects.isNull(imageFile.getOriginalFilename())) {
            throw S3ErrorCode.IMAGE_FILE_EMPTY.toException();
        }
        String originalFilename = validateImageFilename(imageFile.getOriginalFilename());
        String storedFilename = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(AWSProperties.getS3().getBucketName())
                    .key(storedFilename)
                    .contentType(imageFile.getContentType())
                    .contentLength(imageFile.getSize())
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(imageFile.getBytes()));
        } catch (Exception e) {
            throw S3ErrorCode.S3_BUCKET_UPLOAD_FAILED.toException();
        }

        String fileUrl = s3Client.utilities().getUrl(
                GetUrlRequest.builder()
                        .bucket(AWSProperties.getS3().getBucketName())
                        .key(storedFilename)
                        .build()).toString();

        return ImageUploadInfo.toResponse(originalFilename, fileUrl);
    }

    private String validateImageFilename(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw S3ErrorCode.NO_IMAGE_FILE_EXTENSION.toException();
        }
        String fileExtension = filename.substring(lastDotIndex + 1).toLowerCase();
        if (!ALLOWED_FILE_EXTENSIONS.contains(fileExtension)) {
            throw S3ErrorCode.INVALID_IMAGE_FILE_EXTENSION.toException();
        }

        return filename;
    }

    public void deleteImage(String imagePath) {
        String key = extractKeyFromImageUrl(imagePath);
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(AWSProperties.getS3().getBucketName())
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw S3ErrorCode.S3_BUCKET_DELETE_FAILED.toException();
        }
    }

    private String extractKeyFromImageUrl(String imagePath) {
        try {
            URL url = new URL(imagePath);
            String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);

            return decodingKey.substring(1);
        } catch (MalformedURLException e){
            throw S3ErrorCode.INVALID_IMAGE_URL.toException();
        }
    }
}
