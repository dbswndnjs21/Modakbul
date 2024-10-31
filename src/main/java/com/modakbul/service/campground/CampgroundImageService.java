package com.modakbul.service.campground;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.image.CampgroundImage;
import com.modakbul.repository.campground.CampgroundImageRepository;
import com.modakbul.repository.campground.CampgroundRepository;
import com.modakbul.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class CampgroundImageService {
    @Autowired
    private CampgroundImageRepository campgroundImageRepository;
    @Autowired
    private CampgroundRepository campgroundRepository;
    @Autowired
    private FileUploadService fileUploadService;

    public String saveCampgroundImages(Long campgroundId, List<MultipartFile> images, String filePath) {
        campgroundImageRepository.findByCampgroundId(campgroundId);
        File destDir = new File(filePath);

        // 파일 저장 경로가 존재하지 않으면 생성
        if (!destDir.exists()) {
            boolean created = destDir.mkdirs(); // 상위 디렉토리까지 모두 생성
            if (!created) {
                throw new RuntimeException("디렉토리 생성 실패: " + filePath);
            }
        }
        int imageOrder = 1;
        // 파일 저장
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                // 원래 파일 이름의 확장자 추출
                String originalFilename = image.getOriginalFilename();
                String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";

                // UUID 생성
                String uuidFileName = UUID.randomUUID().toString() + extension; // UUID + 원래 확장자


                // S3에 파일 업로드
                try {
                    String fileUrl = fileUploadService.uploadFile(image,uuidFileName); // S3에 파일 업로드
                    Campground campground = new Campground();
                    campground.setId(campgroundId);
                    CampgroundImage campgroundImage = CampgroundImage.builder()
                            .fileName(originalFilename)
                            .saveFileName(uuidFileName)
                            .imagePath(filePath)
                            .imageOrder(imageOrder++)
                            .campground(campground)
                            .build();

                    campgroundImageRepository.save(campgroundImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return "파일 업로드 성공";
    }
}