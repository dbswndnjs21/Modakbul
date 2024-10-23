package com.modakbul.service.campground;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.image.CampgroundImage;
import com.modakbul.repository.campground.CampgroundImageRepository;
import com.modakbul.repository.campground.CampgroundRepository;
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
                String originalFileName = image.getOriginalFilename();
                String saveFileName = UUID.randomUUID() + "_" + originalFileName; // 저장할 파일명 생성

                // 실제 파일 저장
                File savedFile = new File(destDir, saveFileName);
                try {
                    image.transferTo(savedFile);
                } catch (IOException e) {
                    System.err.println("파일업로드실패" + originalFileName + "- " + e.getMessage());
                }
                Campground campground = new Campground();
                campground.setId(campgroundId);
                CampgroundImage campgroundImage = CampgroundImage.builder()
                        .fileName(originalFileName)
                        .saveFileName(saveFileName)
                        .imagePath(filePath)
                        .imageOrder(imageOrder++)
                        .campground(campground)
                        .build();
                campgroundImageRepository.save(campgroundImage);
            }
        }

        return "파일 업로드 성공";
    }
}