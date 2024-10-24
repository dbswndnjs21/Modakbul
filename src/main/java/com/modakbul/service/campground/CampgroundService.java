package com.modakbul.service.campground;

import com.modakbul.dto.campground.*;
import com.modakbul.dto.campsite.CampsiteDto;
import com.modakbul.dto.member.MemberDto;
import com.modakbul.entity.campground.*;
import com.modakbul.entity.campsite.CampsitePrice;
import com.modakbul.entity.image.CampgroundImage;
import com.modakbul.entity.member.Host;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.campground.*;
import com.modakbul.repository.campsite.CampsitePriceRepository;
import com.modakbul.repository.campsite.CampsiteRepository;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.campsite.CampsiteService;
import com.modakbul.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CampgroundService {
    @Autowired
    private CampgroundRepository campgroundRepository;
    @Autowired
    private CampsiteRepository campsiteRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationDetailService locationDetailService;
    @Autowired
    private CampsiteService campsiteService;
    @Autowired
    private CampgroundSuboptionRepository campgroundSuboptionRepository;
    @Autowired
    private CampgroundOptionRepository campgroundOptionRepository;
    @Autowired
    private CampgroundOptionLinkRepository campgroundOptionLinkRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationDetailRepository locationDetailRepository;
    @Autowired
    private CampgroundImageService campgroundImageService;
    @Autowired
    private CampgroundImageRepository campgroundImageRepository;
    @Autowired
    private CampsitePriceRepository campsitePriceRepository;

    public List<CampgroundDto> getAllCampgrounds() {
        List<Campground> campgrounds = campgroundRepository.findAll();

        List<CampgroundDto> campgroundDtos = campgrounds.stream()
                .map(campground -> new CampgroundDto(campground))
                .collect(Collectors.toList());
        return campgroundDtos;
    }

    public CampgroundDto getCampgroundById(Long id) {
        Campground campground = campgroundRepository.findById(id).orElse(null);

        if(campground != null) {
            return new CampgroundDto(campground);
        }else {
            return null;
        }
    }

    public CampgroundDto createCampground(CampgroundDto campgroundDto, String sido, String sigungu, List<Integer> subOptionIds, List<MultipartFile> images, String path) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        MemberDto member = new MemberDto(principal.getMember());

        Host host = new Host();
        host.setId(member.getId());

        // Location 설정
        LocationDto location = locationService.findOnCreateLocation(sido);
        LocationDetailDto locationDetailDto = locationDetailService.findOrCreateLocationDetail(sigungu, location);

        // LocationDetail 생성 및 저장
        LocationDetail locationDetail = locationDetailDto.toEntity(location.toEntity());

        // Campground 엔티티 생성
        Campground campground = campgroundDto.toEntity(locationDetail, host);

        // LocationDetail을 Campground에 설정
        campground.setLocationDetail(locationDetail);

        // Campground 저장
        Campground savedCampground = campgroundRepository.save(campground);

        campgroundImageService.saveCampgroundImages(savedCampground.getId(), images, path);

        CampgroundDto result = new CampgroundDto(savedCampground);

        createCampgroundOptionLink(subOptionIds, result);
        return result;
    }

    public void createCampgroundOptionLink(List<Integer> subOptionIds, CampgroundDto campgroundDto){
        LocationDetail locationDetail = new LocationDetail();
        Host host = new Host();
        host.setId(campgroundDto.getHostId());
        Campground campground = campgroundDto.toEntity(locationDetail, host);

        List<CampgroundSuboption> allSubOptions = campgroundSuboptionRepository.findAll();

        List<CampgroundOptionLink> links = new ArrayList<>();

        // 3. 서브 옵션을 체크하여 CampgroundOptionLink에 추가
        for (CampgroundSuboption subOption : allSubOptions) {
            CampgroundOptionLink link = new CampgroundOptionLink();
            link.setCampground(campground);
            link.setCampgroundSuboption(subOption);
            // 4. 선택된 ID와 비교하여 isExist 값 설정
            if (subOptionIds.contains(subOption.getId())) {
                link.setExist(true); // 선택됨
            } else {
                link.setExist(false); // 선택되지 않음
            }
            links.add(link);
        }

        campgroundOptionLinkRepository.saveAll(links);
    }

    public List<CampgroundDto> getCampgroundWithImages(List<CampgroundDto> campgrounds) {

        // 각 캠핑장 DTO에 대해 이미지를 조회하고 설정
        for (CampgroundDto dto : campgrounds) {
            // 해당 캠핑장 ID를 사용하여 이미지 리스트 조회
            List<CampgroundImage> campgroundImages = campgroundImageRepository.findByCampgroundId(dto.getId());
            dto.setCampgroundImages(campgroundImages);
        }

        return campgrounds;  // 이미지가 추가된 DTO 리스트 반환
    }


    public List<CampgroundDto> searchCampgrounds(String query) {
        // 캠핑장 목록을 가져오고, 이름 또는 설명으로 필터링
        List<Campground> campgrounds = campgroundRepository.findByCampgroundNameContainingIgnoreCase(query);

        List<CampgroundDto> campgroundDtos = campgrounds.stream()
                .map(campground -> new CampgroundDto(campground))
                .collect(Collectors.toList());
        return campgroundDtos;

    }
    public List<CampgroundDto> searchCampgrounds(String query, Integer locationDetailId) {
        List<Campground> campgrounds =  campgroundRepository.findByCampgroundNameContainingAndLocationDetail(query, locationDetailId);

        List<CampgroundDto> campgroundDtos = campgrounds.stream()
                .map(campground -> new CampgroundDto(campground))
                .collect(Collectors.toList());
        return campgroundDtos;
    }

    public int getLowestPrice(CampgroundDto campground, LocalDate checkInDate, LocalDate checkOutDate) {
        List<CampsiteDto> campsites = campsiteService.findCampsitesByCampgroundId(campground.getId());
        if(campsites.isEmpty()){
            return 0;
        }
        int lowestPrice = Integer.MAX_VALUE;

        for (CampsiteDto campsite : campsites) {
            int totalPrice = campsiteService.calculateTotalPrice(campsite.getId(), checkInDate, checkOutDate);
            if(totalPrice <= lowestPrice){
                lowestPrice = totalPrice;
            }
        }
        return lowestPrice;
    }
    public List<CampgroundDto> getCampgroundsByHostId(Long hostId) {
        List<Campground> campgrounds = campgroundRepository.findIdByHostId(hostId);

        List<CampgroundDto> campgroundDtos = campgrounds.stream()
                .map(campground -> new CampgroundDto(campground))
                .collect(Collectors.toList());
        return campgroundDtos;
    }

    public List<CampgroundSuboptionDto> getCampgroundSuboptions(){
        List<CampgroundSuboption> suboptions = campgroundSuboptionRepository.findAll();

        List<CampgroundSuboptionDto> campgroundSuboptionDtos = suboptions.stream()
                .map(suboption -> new CampgroundSuboptionDto(suboption))
                .collect(Collectors.toList());
        return campgroundSuboptionDtos;
    }

    public List<CampgroundOptionDto> getCampgroundOptions() {
        List<CampgroundOption> campgroundOptions = campgroundOptionRepository.findAll();

        List<CampgroundOptionDto> campgroundOptionDtos = campgroundOptions.stream()
                .map(option -> new CampgroundOptionDto(option))
                .collect(Collectors.toList());
        return campgroundOptionDtos;
    }
    
    public void approveCampground(Long id) {
        Campground campground = campgroundRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid campground ID: " + id));
        campground.setApprove(2);  // 승인 상태를 2로 변경
        campgroundRepository.save(campground);  // 변경된 상태를 저장
    }

    public String getLocationDetailSigungu(Long campgroundId){
        CampgroundDto campgroundDto = getCampgroundById(campgroundId);
        LocationDetail locationDetail = locationDetailRepository.findById(campgroundDto.getLocationDetailId());

        return locationDetail.getSigungu();
    }

    public String getLocationSido(Long campgroundId){
        CampgroundDto campgroundDto = getCampgroundById(campgroundId);
        LocationDetail locationDetail = locationDetailRepository.findById(campgroundDto.getLocationDetailId());

        return locationDetail.getLocation().getSido();
    }

    public CampgroundDto editCampground(CampgroundDto campground, String sido, String sigungu, List<Integer> subOptionIds) {
        // 기존 캠핑장 조회
        Campground existingCampground = campgroundRepository.findById(campground.getId())
                .orElseThrow(() -> new IllegalArgumentException("캠핑장을 찾을 수 없습니다. ID: " + campground.getId()));

        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        MemberDto member = new MemberDto(principal.getMember());

        // 수정 권한 확인 (예시: 현재 로그인된 사용자가 호스트인지 확인)
        if (!existingCampground.getHost().getId().equals(member.getId())) {
            throw new SecurityException("캠핑장 수정 권한이 없습니다.");
        }

        // Location 수정
        LocationDto location = locationService.findOnCreateLocation(sido);
        LocationDetailDto locationDetailDto = locationDetailService.findOrCreateLocationDetail(sigungu, location);

        LocationDetail updatedLocationDetail = locationDetailDto.toEntity(location.toEntity());

        // Campground 업데이트
        existingCampground.updateFromDto(campground);  // Dto의 값으로 엔티티 업데이트

        // LocationDetail 및 Host 업데이트
        existingCampground.setLocationDetail(updatedLocationDetail);

        // 캠핑장 옵션 수정
        editCampgroundOptionLink(subOptionIds, campground);

        // 변경된 캠핑장 저장
        Campground updatedCampground = campgroundRepository.save(existingCampground);

        return new CampgroundDto(updatedCampground);
    }

    public void editCampgroundOptionLink(List<Integer> subOptionIds, CampgroundDto campgroundDto){
        LocationDetail locationDetail = new LocationDetail();
        Host host = new Host();
        host.setId(campgroundDto.getHostId());
//        Campground campground = campgroundDto.toEntity(locationDetail, host);

        // 해당 캠핑장의 기존 옵션 링크들을 모두 조회
        List<CampgroundOptionLink> existingLinks = campgroundOptionLinkRepository.findByCampgroundId(campgroundDto.getId());

        // 3. 서브 옵션에 따라 isExist 값을 업데이트
        for (CampgroundOptionLink link : existingLinks) {
            CampgroundSuboption subOption = link.getCampgroundSuboption();

            // 4. 선택된 서브 옵션 ID와 비교하여 is_exist 값 수정
            if (subOptionIds != null && subOptionIds.contains(subOption.getId())) {
                link.setExist(true);  // 선택된 서브 옵션이면 is_exist를 true로 설정
            } else {
                link.setExist(false); // 선택되지 않은 서브 옵션이면 false로 설정
            }
        }

        campgroundOptionLinkRepository.saveAll(existingLinks);
    }

    public Map<LocalDate, Integer> getCampgroundLowestPrices(CampgroundDto campground) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(92);

        List<CampsiteDto> campsites = campsiteService.findCampsitesByCampgroundId(campground.getId());
        if(campsites.isEmpty()){
            return null;
        }
        Map<LocalDate, Integer> priceMap = new HashMap<>();

        List<LocalDate> dateList = DateUtil.getDatesBetween(today, endDate);
        for (LocalDate date : dateList) {
            Integer price;
            Integer lowestPrice = Integer.MAX_VALUE;
            for (CampsiteDto campsite : campsites) {
                CampsitePrice campsitePrice = campsitePriceRepository.findByCampsiteIdAndIdPriceDate(campsite.getId(), date);
                if(campsitePrice != null){
                    price = campsitePrice.getPrice();
                    if(price <= lowestPrice){
                        lowestPrice = price;
                    }
                }
            }
            if(lowestPrice != Integer.MAX_VALUE){
                priceMap.put(date, lowestPrice);
            }
        }
        return priceMap;
    }
}
