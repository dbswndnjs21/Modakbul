package com.modakbul.service.campground;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.campground.CampgroundOptionLinkDto;
import com.modakbul.entity.campground.CampgroundOptionLink;
import com.modakbul.repository.campground.CampgroundOptionLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampgroundOptionLinkService {
    @Autowired
    private CampgroundOptionLinkRepository campgroundOptionLinkRepository;

    public List<CampgroundOptionLinkDto> getActiveLinksByCampgroundId(Long campgroundId){
        List<CampgroundOptionLink> campgroundOptionLinks = campgroundOptionLinkRepository.findByCampgroundIdAndIsExistTrue(campgroundId);

        List<CampgroundOptionLinkDto> campgroundOptionLinksDtos = campgroundOptionLinks.stream()
                .map(campgroundOptionLink -> new CampgroundOptionLinkDto(campgroundOptionLink))
                .collect(Collectors.toList());
        return campgroundOptionLinksDtos;
    }
}
