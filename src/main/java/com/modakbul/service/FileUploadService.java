package com.modakbul.service; // 패키지 선언

import com.amazonaws.services.s3.AmazonS3; // Amazon S3 클라이언트 인터페이스
import com.amazonaws.services.s3.model.ObjectMetadata; // S3 객체 메타데이터 클래스
import lombok.RequiredArgsConstructor; // 생성자 주입을 위한 어노테이션
import org.springframework.beans.factory.annotation.Value; // 스프링에서 프로퍼티 주입을 위한 어노테이션
import org.springframework.stereotype.Service; // 서비스 클래스임을 나타내는 어노테이션
import org.springframework.web.multipart.MultipartFile; // 파일 업로드를 위한 MultipartFile 클래스

import java.io.IOException; // 입출력 예외 처리

@Service // 이 클래스가 서비스 클래스임을 나타냄
@RequiredArgsConstructor // 필수 필드를 가진 생성자 생성
public class FileUploadService {

    private final AmazonS3 amazonS3Client; // Amazon S3 클라이언트를 주입받음

    @Value("${cloud.aws.s3.bucket}") // 프로퍼티 파일에서 버킷 이름을 주입
    private String bucket;

    @Value("${cloud.aws.region.static}") // 프로퍼티 파일에서 리전(지역)을 주입
    private String region;

    // 파일을 S3에 업로드하는 메서드
    public String uploadFile(MultipartFile file) throws IOException {
        // 업로드할 파일의 원본 파일명 가져오기
        String fileName = file.getOriginalFilename();

        // S3에 파일 업로드를 위한 메타데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType()); // 파일의 콘텐츠 타입 설정
        metadata.setContentLength(file.getSize()); // 파일의 크기 설정
        // S3에 파일 업로드
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

        // S3 URL 생성
        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);

        return fileUrl; // 생성된 S3 URL 반환
    }
}
