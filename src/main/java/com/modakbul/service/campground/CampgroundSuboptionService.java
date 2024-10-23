package com.modakbul.service.campground;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.campground.CampgroundOptionLinkDto;
import com.modakbul.dto.campground.CampgroundSuboptionDto;
import com.modakbul.entity.campground.CampgroundOptionLink;
import com.modakbul.entity.campground.CampgroundSuboption;
import com.modakbul.repository.campground.CampgroundOptionLinkRepository;
import com.modakbul.repository.campground.CampgroundSuboptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampgroundSuboptionService {
    @Autowired
    private CampgroundSuboptionRepository campgroundSuboptionRepository;
    @Autowired
    private CampgroundOptionLinkService campgroundOptionLinkService;

    public List<CampgroundSuboptionDto> getSubOptionByCampgroundId(Long campgroundId) {
        // 활성화된 링크 가져오기
        List<CampgroundOptionLinkDto> campgroundOptionLinkDtos = campgroundOptionLinkService.getActiveLinksByCampgroundId(campgroundId);

        // 서브 옵션 ID 목록 생성
        List<Integer> suboptionIds = campgroundOptionLinkDtos.stream()
                .map(link -> link.getCampgroundSubOptionId()) // 서브 옵션 ID 가져오기
                .collect(Collectors.toList());

        // 서브 옵션 조회
        List<CampgroundSuboption> campgroundSuboptions = campgroundSuboptionRepository.findAllById(suboptionIds);

        // 서브 옵션을 DTO로 변환
        List<CampgroundSuboptionDto> suboptionDtos = campgroundSuboptions.stream()
                .map(suboption -> {
                    CampgroundSuboptionDto dto = new CampgroundSuboptionDto();
                    dto.setId(suboption.getId());
                    dto.setOptionName(suboption.getOptionName()); // 이름 설정
                    return dto;
                })
                .collect(Collectors.toList());

        return suboptionDtos;
    }

    public List<Integer> getSuboptionId(Long campgroundId) {
        // 활성화된 링크 가져오기
        List<CampgroundOptionLinkDto> campgroundOptionLinkDtos = campgroundOptionLinkService.getActiveLinksByCampgroundId(campgroundId);

        // 서브 옵션 ID 목록 생성
        List<Integer> suboptionIds = campgroundOptionLinkDtos.stream()
                .map(CampgroundOptionLinkDto::getCampgroundSubOptionId) // 서브 옵션 ID 가져오기
                .collect(Collectors.toList());

        return suboptionIds; // 서브 옵션 ID 목록 반환
    }

}
