package com.modakbul.service.freeboard;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.modakbul.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeboardService {
	private final FreeboardRepository freeboardRepository;
	private final FreeboardImageRepository freeboardImageRepository;
	private final FileUploadService fileUploadService; // 추가

	public String writeFreeboard(FreeboardDto freeboardDto, Long memberId, List<MultipartFile> files) {
		// Freeboard 엔티티 생성
		Freeboard freeboard = freeboardDto.toEntity(memberId);
		freeboard.setCreatedAt(LocalDateTime.now());
		freeboard.setUpdatedAt(LocalDateTime.now());

		// Freeboard 저장
		Freeboard savedFreeboard = freeboardRepository.save(freeboard);

		// 이미지 처리
		int imageOrder = 1; // 이미지 순서 변수 초기화
		for (MultipartFile mf : files) {
			if (mf.isEmpty()) {
				continue; // 비어있는 파일은 건너뜀
			}
			try {
				// 원래 파일 이름의 확장자 추출
	            String originalFilename = mf.getOriginalFilename();
	            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";

	            // UUID 생성
	            String uuidFileName = UUID.randomUUID().toString() + extension; // UUID + 원래 확장자

				
				// S3에 파일 업로드
				String fileUrl = fileUploadService.uploadFile(mf,uuidFileName); // S3에 파일 업로드

				// FreeboardImage DTO 생성
				FreeboardImageDto freeboardImageDto = FreeboardImageDto.builder()
						.fileName(mf.getOriginalFilename())
						.saveFileName(uuidFileName) // S3의 파일 URL
						.imagePath(fileUrl) // S3의 파일 URL
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


	 
	public List<FreeboardDto> findWithImagesPaged(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
	    Page<Freeboard> freeboardPage = freeboardRepository.findAll(pageable);

	    List<Freeboard> freeboards = freeboardPage.getContent();

	    System.out.println("Page: " + page + ", Loaded Posts: " + freeboards.size());

	    // 엔티티에서 DTO로 변환하여 반환
	    List<FreeboardDto> freeboardDtos = freeboards.stream().map(freeboard -> {
	        List<FreeboardImage> images = freeboardImageRepository.findByFreeboardIdOrderByImageOrderAsc(freeboard.getId());

	        // FreeboardImage 엔티티를 FreeboardImageDto로 변환
	        List<FreeboardImageDto> imageDtos = images.stream().map(image -> FreeboardImageDto.builder()
	                .id(image.getId())
	                .freeboardId(image.getFreeboard().getId())
	                .fileName(image.getFileName())
	                .saveFileName(image.getSaveFileName())
	                .imagePath(image.getImagePath())
	                .imageOrder(image.getImageOrder())
	                .build()).toList();

	        // Freeboard 엔티티를 FreeboardDto로 변환
	        return FreeboardDto.builder()
	                .id(freeboard.getId())
	                .memberId(freeboard.getMember().getId())
	                .title(freeboard.getTitle())
	                .content(freeboard.getContent())
	                .createdAt(freeboard.getCreatedAt())
	                .updatedAt(freeboard.getUpdatedAt())
	                .images(imageDtos)  // 변환된 이미지 DTO 설정
	                .build();
	    }).toList();

	    return freeboardDtos;
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
	    
	    public void deleteBoard(Long id) {
	    	
	    	 // 게시글 조회
	        Optional<Freeboard> freeboardOpt = freeboardRepository.findById(id);
	        if (!freeboardOpt.isPresent()) {
	            throw new EntityNotFoundException("게시글이 존재하지 않습니다."); // 예외 처리
	        }

	        Freeboard freeboard = freeboardOpt.get();
	        
	        // 관련 이미지 삭제 (옵션)
	        List<FreeboardImage> images = freeboardImageRepository.findByFreeboardId(freeboard.getId());
	        for (FreeboardImage image : images) {
	        	// S3에서 이미지 삭제
	            fileUploadService.deleteFile(image.getSaveFileName());
	            // 데이터베이스에서 이미지 삭제
	            freeboardImageRepository.delete(image);
	        }
	        // 게시글 삭제
	        freeboardRepository.delete(freeboard);
	    }
	    
	    public void updateSelect(Long id) {
	    	Optional<Freeboard> freeboardOpt = freeboardRepository.findById(id);
	    	if (!freeboardOpt.isPresent()) {
	            throw new EntityNotFoundException("게시글이 존재하지 않습니다."); // 예외 처리
	        }
	    	Freeboard freeboard = freeboardOpt.get();
	    	List<FreeboardImage> images = freeboardImageRepository.findByFreeboardId(freeboard.getId());
	    }
	    
	    // 게시글 수정 메서드
//	    public String updateImageFreeboard(FreeboardDto freeboardDto, Long memberId, List<MultipartFile> files, String[] removedImages, String filePath) {
//	        // 1. 수정할 게시글 찾기
//	        Optional<Freeboard> optionalBoard = freeboardRepository.findById(freeboardDto.getId());
//	        if (!optionalBoard.isPresent()) {
//	            return "해당 게시글을 찾을 수 없습니다.";
//	        }
//
//	        Freeboard freeboard = optionalBoard.get();
//
//	        // 2. 게시글 내용 수정
//	        freeboard.setTitle(freeboardDto.getTitle());
//	        freeboard.setContent(freeboardDto.getContent());
//	        freeboardRepository.save(freeboard);
//
//	        // 3. 기존 이미지 중 삭제되지 않은 이미지는 그대로 유지
//	        List<FreeboardImage> existingImages = freeboardImageRepository.findByFreeboard(freeboard);
//	        List<FreeboardImage> imagesToKeep = new ArrayList<>();
//
//	        if (existingImages != null && !existingImages.isEmpty()) {
//	            for (FreeboardImage image : existingImages) {
//	                boolean toRemove = false;
//	                if (removedImages != null) {
//	                    for (String removedImage : removedImages) {
//	                        if (image.getSaveFileName().equals(removedImage)) {
//	                            toRemove = true;
//	                            break;
//	                        }
//	                    }
//	                }
//	                if (!toRemove) {
//	                    imagesToKeep.add(image); // 삭제되지 않은 이미지는 유지 목록에 추가
//	                }
//	            }
//	        }
//
//	        // 4. 새로 업로드된 이미지 처리
//	        if (files != null && !files.isEmpty()) {
//	            for (MultipartFile file : files) {
//	                if (!file.isEmpty()) {
//	                    try {
//	                        // 새로운 파일명 생성
//	                        String originalFileName = file.getOriginalFilename();
//	                        String saveFileName = UUID.randomUUID().toString() + "_" + originalFileName;
//
//	                        // 파일 저장
//	                        file.transferTo(new File(filePath + saveFileName));
//
//	                        // 이미지 정보 저장
//	                        FreeboardImage newImage = new FreeboardImage();
//	                        newImage.setFreeboard(freeboard);
//	                        newImage.setFileName(saveFileName);
//	                        newImage.setSaveFileName(saveFileName);
//	                        imagesToKeep.add(newImage); // 새로 추가된 이미지도 유지 목록에 포함
//
//	                    } catch (IOException e) {
//	                        e.printStackTrace();
//	                        return "파일 업로드 중 오류가 발생했습니다.";
//	                    }
//	                }
//	            }
//	        }
//
//	        // 5. 실제로 유지할 이미지만 저장 (기존 이미지 중 유지될 이미지 + 새로 추가된 이미지)
//	        freeboardImageRepository.deleteByFreeboard(freeboard); // 모든 기존 이미지 삭제
//	        freeboardImageRepository.saveAll(imagesToKeep); // 유지할 이미지를 다시 저장
//
//	        return "게시글 수정이 완료되었습니다.";
//	    }
	    
	    public String updateFreeboard(FreeboardDto freeboardDto,Long memberId ) {
	    	Optional<Freeboard> optionalBoard = freeboardRepository.findById(freeboardDto.getId());
	        if (!optionalBoard.isPresent()) {
	            return "해당 게시글을 찾을 수 없습니다.";
	        }
	        Freeboard freeboard = optionalBoard.get();
	        
	        freeboard.setTitle(freeboardDto.getTitle());
	        freeboard.setContent(freeboardDto.getContent());
	        freeboardRepository.save(freeboard);
	        
	        return "게시글 수정이 완료되었습니다.";
	    }
	    
	    private void saveFilesToS3(List<MultipartFile> files, Freeboard savedFreeboard) {
	        int imageOrder = 1; // 이미지 순서 변수 초기화
	        for (MultipartFile mf : files) {
	            if (mf.isEmpty()) {
	                continue; // 비어있는 파일은 건너뜀
	            }
	            String orgFileName = mf.getOriginalFilename(); // 전송된 파일명
	            
	            try {
	            	// 원래 파일 이름의 확장자 추출
	                String originalFileName = mf.getOriginalFilename();
	                String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

	                // UUID 생성
	                String uuidFileName = UUID.randomUUID().toString() + extension; // UUID + 원래 확장자

	                // S3에 파일 업로드
	            	
	                String fileUrl = fileUploadService.uploadFile(mf, uuidFileName);  // S3에 파일 업로드 및 URL 반환

	                // FreeboardImage DTO 생성
	                FreeboardImageDto freeboardImageDto = FreeboardImageDto.builder()
	                        .fileName(orgFileName)
	                        .saveFileName(uuidFileName) // S3의 파일 URL
	                        .imagePath(fileUrl)     // S3의 파일 URL
	                        .imageOrder(imageOrder++) // 이미지 순서
	                        .build();

	                // FreeboardImage 엔티티 생성 및 저장
	                FreeboardImage freeboardImage = freeboardImageDto.toEntity(savedFreeboard); // savedFreeboard 연결
	                freeboardImageRepository.save(freeboardImage);
	            } catch (IOException e) {
	                e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
	            }
	        }
	    }
	    
	    public void deleteFile(String saveFileName, Long freeboardId) {
	        // S3에서 파일 삭제
	        fileUploadService.deleteFile(saveFileName); // S3에서 파일 삭제

	        // 데이터베이스에서 파일 정보 삭제
	        System.out.println("Deleting file: " + saveFileName + " for Freeboard ID: " + freeboardId);
	        freeboardImageRepository.deleteBySaveFileNameAndFreeboardId(saveFileName, freeboardId);
	    }
	    
	    public void updateImageFreeboard(FreeboardDto freeboardDto, Long memberId, List<MultipartFile> files, String[] removedImages) {
	        // 게시글 정보 조회 및 업데이트
	        Freeboard freeboard = freeboardRepository.findById(freeboardDto.getId())
	                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
	        
	        // 게시글 필드 업데이트
	        freeboard.setTitle(freeboardDto.getTitle());
	        freeboard.setContent(freeboardDto.getContent());

	        // S3 파일 저장 처리
	        if (files != null && !files.isEmpty()) {
	            saveFilesToS3(files, freeboard); // S3에 저장 메소드 호출
	        }

	        // S3 이미지 삭제 처리
	        if (removedImages != null && removedImages.length > 0) {
	            for (String removedImage : removedImages) {
	                // 데이터베이스에서 이미지 삭제 및 S3에서 파일 삭제
	                deleteFile(removedImage, freeboard.getId());
	            }
	        }

	        // 업데이트된 게시글 저장
	        freeboardRepository.save(freeboard);
	    }
	public List<Freeboard> getAllPosts() {
		return freeboardRepository.findAll();
	}
}
