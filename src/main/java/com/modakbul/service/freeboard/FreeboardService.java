package com.modakbul.service.freeboard;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.dto.freeboard.FreeboardDto;
import com.modakbul.dto.image.FreeboardImageDto;
import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.image.FreeboardImage;
import com.modakbul.repository.freeboard.FreeboardImageRepository;
import com.modakbul.repository.freeboard.FreeboardRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FreeboardService {
	@Autowired FreeboardRepository freeboardRepository;
	@Autowired FreeboardImageRepository freeboardImageRepository;
	@Value("${file.path}")
	private String filePath;
		
	 public String writeFreeboard(FreeboardDto freeboardDto, Long memberId, List<MultipartFile> files, String filePath) {
	        // Freeboard 엔티티 생성
	        Freeboard freeboard = freeboardDto.toEntity(memberId);
	        freeboard.setCreatedAt(LocalDateTime.now());
	        freeboard.setUpdatedAt(LocalDateTime.now());
	        // Freeboard 저장
	        Freeboard savedFreeboard = freeboardRepository.save(freeboard);
	        // 파일 저장 경로 확인 및 생성
	        File destDir = new File(filePath);
	        if (!destDir.exists()) {
	            destDir.mkdirs(); // 부모 디렉토리도 포함하여 모든 경로 생성
	        }
	        // 이미지 처리
	        int imageOrder = 1; // 이미지 순서 변수 초기화
	        for (MultipartFile mf : files) {
	            if (mf.isEmpty()) {
	                continue; // 비어있는 파일은 건너뜀
	            }
	            String orgFileName = mf.getOriginalFilename(); // 전송된 파일명
	            String saveFileName = UUID.randomUUID() + "_" + orgFileName; // 저장할 파일명
	            try {
	                // 파일 저장
	                File f = new File(destDir, saveFileName); // 저장할 정보를 갖는 파일 객체
	                mf.transferTo(f); // 업로드한 파일을 f에 복사하기
	                // FreeboardImage DTO 생성
	                FreeboardImageDto freeboardImageDto = FreeboardImageDto.builder()
	                        .fileName(orgFileName)
	                        .saveFileName(saveFileName)
	                        .imagePath(filePath)
	                        .imageOrder(imageOrder++) // 이미지 순서
	                        .build();
	                // FreeboardImage 엔티티 생성 및 저장
	                FreeboardImage freeboardImage = freeboardImageDto.toEntity(savedFreeboard); // savedFreeboard 연결
	                freeboardImageRepository.save(freeboardImage);
	            } catch (IOException e) {
	                e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
	                return "파일 업로드 실패: " + e.getMessage(); // 실패 메시지 반환
	            }
	        }

	        return "파일 업로드 성공"; // 성공 메시지 반환
	    }
	 
	 public List<Freeboard> findAllWithImages() {
	        List<Freeboard> list = freeboardRepository.findAll();
	        for (Freeboard freeboard : list) {
	            // 정렬된 이미지를 가져옴
	            List<FreeboardImage> images = freeboardImageRepository.findByFreeboardIdOrderByImageOrderAsc(freeboard.getId());
	            freeboard.setImages(images); // Freeboard 엔티티에 이미지 설정
	        }
	        return list;
	    }
	 
}
