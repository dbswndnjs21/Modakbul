package com.modakbul.service.campsite;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.image.CampgroundImage;
import com.modakbul.entity.image.CampsiteImage;
import com.modakbul.repository.campground.CampgroundImageRepository;
import com.modakbul.repository.campground.CampgroundRepository;
import com.modakbul.repository.campsite.CampsiteImageRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import com.modakbul.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class CampsiteImageService {
    @Autowired
    private CampsiteRepository campsiteRepository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private CampsiteImageRepository campsiteImageRepository;

    public String saveCampsiteImages(Long campsiteId, List<MultipartFile> images) {
        campsiteImageRepository.findByCampsiteId(campsiteId);

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
                    Campsite campsite = new Campsite();
                    campsite.setId(campsiteId);
                    CampsiteImage campsiteImage = CampsiteImage.builder()
                            .fileName(originalFilename)
                            .saveFileName(uuidFileName)
                            .imagePath(fileUrl)
                            .imageOrder(imageOrder++)
                            .campsite(campsite)
                            .build();

                    campsiteImageRepository.save(campsiteImage)
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return "파일 업로드 성공";
    }
}