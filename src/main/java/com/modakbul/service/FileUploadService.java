package com.modakbul.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}") // 리전 추가
    private String region;

    // UUID를 포함한 파일 이름으로 업로드
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        // S3에 파일 업로드
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        // S3 URL 생성
        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);

        return fileUrl; // 생성된 S3 URL 반환
    }
    
    public void deleteFile(String fileUrl) {
        // S3에서 파일 삭제 로직 구현
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1); // 파일 이름 추출
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName)); // 'bucketName'을 'bucket'으로 변경
    }
    
}
