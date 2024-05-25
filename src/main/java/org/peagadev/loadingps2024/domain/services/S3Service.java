package org.peagadev.loadingps2024.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.peagadev.loadingps2024.exceptions.CloudOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.identity.spi.AwsCredentialsIdentity;
import software.amazon.awssdk.identity.spi.AwsSessionCredentialsIdentity;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3Service {

    private final S3Client s3Client;
    @Value("${aws.credentials.BUCKET-NAME}")
    private String bucketName;
    @Value("${aws.credentials.BUCKET-REGION}")
    private String bucketRegion;

    public String putImage(MultipartFile file,String imgKey){
        imgKey = "images/"+imgKey;
        var obj = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(imgKey)
                .contentType(file.getContentType())
                .build();
        try {
            s3Client.putObject(obj,RequestBody.fromBytes(file.getBytes()));
        }catch (IOException | SdkException e){
            throw new CloudOperationException(e.getMessage());
        }
        log.info("Image uploaded successfully");
        return String.format("https://s3.%s.amazonaws.com/%s/%s", bucketRegion, bucketName, imgKey);
    }
    //missing delete img
}
