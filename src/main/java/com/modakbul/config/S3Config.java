package com.modakbul.config; // 패키지 선언

import com.amazonaws.auth.AWSStaticCredentialsProvider; // AWS 자격 증명 제공자 클래스
import com.amazonaws.auth.BasicAWSCredentials; // 기본 AWS 자격 증명 클래스
import com.amazonaws.services.s3.AmazonS3; // Amazon S3 클라이언트 인터페이스
import com.amazonaws.services.s3.AmazonS3ClientBuilder; // Amazon S3 클라이언트 빌더 클래스
import org.springframework.beans.factory.annotation.Value; // 스프링에서 프로퍼티 주입을 위한 어노테이션
import org.springframework.context.annotation.Bean; // 스프링에서 빈을 생성하기 위한 어노테이션
import org.springframework.context.annotation.Configuration; // 스프링 설정 클래스를 나타내는 어노테이션

@Configuration // 이 클래스가 설정 클래스임을 나타냄
public class S3Config {

   @Value("${cloud.aws.credentials.access-key}") // 프로퍼티 파일에서 접근 키를 주입
   private String accessKey;

   @Value("${cloud.aws.credentials.secret-key}") // 프로퍼티 파일에서 비밀 키를 주입
   private String secretKey;

   @Value("${cloud.aws.region.static}") // 프로퍼티 파일에서 리전(지역)을 주입
   private String region;

   @Bean // 이 메서드가 스프링의 빈으로 등록됨을 나타냄
   public AmazonS3 amazonS3Client() {
      // AWS 기본 자격 증명을 생성
      BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
      // Amazon S3 클라이언트를 빌드
      return AmazonS3ClientBuilder.standard()
              .withRegion(region) // 지정된 리전으로 설정
              .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)) // 자격 증명 설정
              .build(); // 클라이언트 생성 및 반환
   }
}
