package com.modakbul.service.freeboard;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
	    
	    private void saveFiles(List<MultipartFile> files, Freeboard savedFreeboard, String filePath) {
	        // 파일 저장 경로 확인 및 생성
	        File destDir = new File(filePath);
	        if (!destDir.exists()) {
	            destDir.mkdirs(); // 부모 디렉토리도 포함하여 모든 경로 생성
	        }

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
	            }
	        }
	    }
	    
	    private void deleteFile(String saveFileName,String filePath,Long id) {
	        // 파일 삭제 로직 구현
	        // 예: 파일 시스템에서 지정된 파일 삭제하기
	        File fileToDelete = new File(filePath,saveFileName);
	        if (fileToDelete.exists()) {
	            boolean deleted = fileToDelete.delete(); // 파일 삭제 시도
	            if (!deleted) {
	                System.out.println("파일 삭제 실패: " + saveFileName);
	            } else {
	                // 파일 삭제가 성공한 경우, 데이터베이스에서도 해당 파일 정보 삭제
	            	System.out.println("Deleting file: " + saveFileName + " for Freeboard ID: " + id);
	            	freeboardImageRepository.deleteBysaveFileNameAndFreeboardId(saveFileName, id);
	            }
	        }
	    }
	    
	    public void updateImageFreeboard(FreeboardDto freeboardDto, Long memberId, List<MultipartFile> files, String[] removedImages, String filePath) {
	        // 게시글 정보 업데이트
	        Freeboard freeboard = freeboardRepository.findById(freeboardDto.getId())
	                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

	        // 게시글 필드 업데이트
	        freeboard.setTitle(freeboardDto.getTitle());
	        freeboard.setContent(freeboardDto.getContent());
	        // 추가적으로 필요한 필드 업데이트

	        // 파일 저장
	        if (files != null && !files.isEmpty()) {
	            saveFiles(files, freeboard, filePath); // 저장 메소드 호출
	        }
	        
	        // 삭제할 이미지 처리
	        if (removedImages != null && removedImages.length > 0) {
	            for (String removedImage : removedImages) {
	                deleteFile(removedImage,filePath, freeboard.getId()); // 파일 삭제 메소드 호출
	            }
	        }

	        // 업데이트된 게시글 저장
	        freeboardRepository.save(freeboard);
	    }
	public List<Freeboard> getAllPosts() {
		return freeboardRepository.findAll();
	}
}
