package com.modakbul.controller; // 패키지 선언

import com.modakbul.service.FileUploadService; // 파일 업로드 서비스를 임포트
import lombok.RequiredArgsConstructor; // 생성자 주입을 위한 어노테이션
import org.springframework.http.HttpStatus; // HTTP 상태 코드를 사용하기 위한 클래스
import org.springframework.http.ResponseEntity; // HTTP 응답 엔티티 클래
import org.springframework.web.bind.annotation.*; // REST API 관련 어노테이션 임포트
import org.springframework.web.multipart.MultipartFile; // 파일 업로드를 위한 MultipartFile 클래스

import java.io.IOException; // 입출력 예외 처리

@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냄
@RequestMapping("/upload") // 요청 URL 경로 설정
@RequiredArgsConstructor // 필수 필드를 가진 생성자 생성
public class FileUploadController {

    private final FileUploadService fileUploadService; // 파일 업로드 서비스를 주입받음

    // 파일 업로드를 처리하는 POST 메서드
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 파일 업로드 서비스 호출하여 파일 업로드
            String fileUrl = fileUploadService.uploadFile(file);
            return ResponseEntity.ok(fileUrl); // 업로드 성공 시 S3 URL 반환
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 내부 서버 오류 응답
        }
    }
}
