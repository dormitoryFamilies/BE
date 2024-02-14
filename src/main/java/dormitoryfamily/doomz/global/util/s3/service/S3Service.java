package dormitoryfamily.doomz.global.util.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import dormitoryfamily.doomz.global.util.s3.exception.FileEmptyException;
import dormitoryfamily.doomz.global.util.s3.exception.FileNotExistsException;
import dormitoryfamily.doomz.global.util.s3.exception.FileUploadFailException;
import dormitoryfamily.doomz.global.util.s3.response.S3UploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public S3UploadResponseDto saveImage(MultipartFile multipartFile) {
        validateFileExists(multipartFile);
        String filename = generateFilename(multipartFile);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(bucketName, filename, inputStream, getObjectMetadata(multipartFile));
        } catch (IOException e) {
            throw new FileUploadFailException();
        }
        return new S3UploadResponseDto(amazonS3.getUrl(bucketName, filename).toString());
    }

    private String generateFilename(MultipartFile multipartFile) {
        return UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
    }

    private ObjectMetadata getObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }

    public void removeImage(String url) {
        String filename = getFilename(url);
        if (!amazonS3.doesObjectExist(bucketName, filename)) {
            throw new FileNotExistsException();
        }

        amazonS3.deleteObject(bucketName, filename);
    }

    private String getFilename(String url) {
        String encodedName = url.substring(url.lastIndexOf("/") + 1);
        try {
            return URLDecoder.decode(encodedName, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {}
        return "";
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new FileEmptyException();
        }
    }
}
