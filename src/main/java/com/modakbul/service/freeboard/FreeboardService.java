package com.modakbul.service.freeboard;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.modakbul.dto.freeboard.FreeboardDto;
import com.modakbul.dto.image.FreeboardImageDto;
import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.image.FreeboardImage;
import com.modakbul.repository.freeboard.FreeboardImageRepository;
import com.modakbul.repository.freeboard.FreeboardRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FreeboardService {
	@Autowired FreeboardRepository freeboardRepository;
	@Autowired FreeboardImageRepository freeboardImageRepository;
	
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
		// 최신 글이 위에 오도록 정렬
		    List<Freeboard> list = freeboardRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt")); //
	        for (Freeboard freeboard : list) {
	            // 정렬된 이미지를 가져옴
	            List<FreeboardImage> images = freeboardImageRepository.findByFreeboardIdOrderByImageOrderAsc(freeboard.getId());
	            freeboard.setImages(images); // Freeboard 엔티티에 이미지 설정
	        }
	        return list;
	    }
	 
	 // 게시글과 이미지 조회
	    public FreeboardDto getPostWithImagesById(Long id) {
	        Freeboard board = freeboardRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));

	        // 이미지 조회 (게시글과 연결된 이미지)
	        List<FreeboardImage> images = freeboardImageRepository.findByFreeboardId(id);
	        List<FreeboardImageDto> imageDtos = images.stream()
	                .map(image -> FreeboardImageDto.builder()
	                        .id(image.getId())
	                        .freeboardId(image.getFreeboard().getId())
	                        .fileName(image.getFileName())
	                        .saveFileName(image.getSaveFileName())
	                        .imagePath(image.getImagePath())
	                        .imageOrder(image.getImageOrder())
	                        .build())
	                .collect(Collectors.toList());
	        
	        // DTO로 변환
	        return FreeboardDto.builder()
	                .id(board.getId())
	                .userId(board.getMember().getUserId())
	                .title(board.getTitle())
	                .content(board.getContent())
	                .createdAt(board.getCreatedAt())
	                .updatedAt(board.getUpdatedAt())
	                .images(imageDtos) // 이미지를 DTO에 추가
	                .build();
	    }
	    
	    public void deleteBoard(Long id, String filePath) {
	    	
	    	 // 게시글 조회
	        Optional<Freeboard> freeboardOpt = freeboardRepository.findById(id);
	        if (!freeboardOpt.isPresent()) {
	            throw new EntityNotFoundException("게시글이 존재하지 않습니다."); // 예외 처리
	        }

	        Freeboard freeboard = freeboardOpt.get();
	        
	        // 관련 이미지 삭제 (옵션)
	        List<FreeboardImage> images = freeboardImageRepository.findByFreeboardId(freeboard.getId());
	        for (FreeboardImage image : images) {
	            // 이미지 파일 시스템에서 삭제 (필요 시)
	             File file = new File(filePath +"\\"+ image.getSaveFileName());
	             file.delete();
	            // 데이터베이스에서 이미지 삭제
	            freeboardImageRepository.delete(image);
	        }
	        // 게시글 삭제
	        freeboardRepository.delete(freeboard);
	    }
	    
}
