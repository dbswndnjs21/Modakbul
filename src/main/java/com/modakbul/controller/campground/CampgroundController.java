package com.modakbul.controller.campground;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.campground.CampgroundOptionDto;
import com.modakbul.dto.campground.CampgroundSuboptionDto;
import com.modakbul.dto.campsite.CampsiteDto;
import com.modakbul.repository.campground.CampgroundOptionLinkRepository;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.campground.CampgroundOptionLinkService;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.campground.CampgroundSuboptionService;
import com.modakbul.service.campground.LocationService;
import com.modakbul.service.campsite.CampsiteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;  // 여기서 @Controller 사용
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/campgrounds")
public class CampgroundController {
    @Value("${file.path}")
    private String filePath;

    private final CampgroundService campgroundService;
    private final CampsiteService campsiteService;
    private final LocationService locationService;
    private final CampgroundOptionLinkRepository campgroundOptionLinkRepository;
    private final CampgroundOptionLinkService campgroundOptionLinkService;
    private final CampgroundSuboptionService campgroundSuboptionService;

    public CampgroundController(CampgroundService campgroundService, CampsiteService campsiteService, LocationService locationService, CampgroundOptionLinkRepository campgroundOptionLinkRepository, CampgroundOptionLinkService campgroundOptionLinkService, CampgroundSuboptionService campgroundSuboptionService, CampgroundSuboptionService campgroundSuboptionService1) {
        this.campgroundService = campgroundService;
        this.campsiteService = campsiteService;
        this.locationService = locationService;
        this.campgroundOptionLinkRepository = campgroundOptionLinkRepository;
        this.campgroundOptionLinkService = campgroundOptionLinkService;
        this.campgroundSuboptionService = campgroundSuboptionService1;
    }

    @ModelAttribute
    public void addAttributes(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        model.addAttribute("member", member);
    }
    
    @GetMapping("/{id}")
    public String showCampgroundDetail(@PathVariable("id") Long id,
                                       @RequestParam(value = "query", required = false) String query,
                                       @RequestParam(value = "checkInDate") LocalDate checkInDate,
                                       @RequestParam(value = "checkOutDate") LocalDate checkOutDate,
                                       @AuthenticationPrincipal CustomUserDetails member,
                                       Model model) {
        if(member != null){
            Long memberId = member.getId();
            model.addAttribute("memberId", memberId);
        }
        // 캠프사이트 정보
        List<CampsiteDto> campsites = campsiteService.findByCampgroundId(id);
        model.addAttribute("campground", campgroundService.getCampgroundById(id));
        model.addAttribute("campsites", campsites);

        Map<Long, Integer> totalPrices = new HashMap<>();

        // 각 캠프사이트에 대한 총 가격 계산
        for (CampsiteDto campsite : campsites) {
            int totalPrice = campsiteService.calculateTotalPrice(campsite.getId(), checkInDate, checkOutDate);
            totalPrices.put(campsite.getId(), totalPrice);
        }

        List<CampgroundSuboptionDto> campgroundSuboptionDtos= campgroundSuboptionService.getSubOptionByCampgroundId(id);
        model.addAttribute("campgroundSubOptions", campgroundSuboptionDtos);
        model.addAttribute("totalPrices", totalPrices);
        return "campground/campgroundDetail";
    }

    // 캠핑장 추가 폼 페이지로 이동
    @GetMapping("/add")
    public String showAddCampgroundForm(Model model) {
        List<CampgroundOptionDto> campgroundOptions = campgroundService.getCampgroundOptions();
        List<CampgroundSuboptionDto> campgroundSuboptions = campgroundService.getCampgroundSuboptions();

        model.addAttribute("campgroundOptions", campgroundOptions);
        model.addAttribute("campgroundSuboptions",campgroundSuboptions);
        model.addAttribute("campground", new CampgroundDto());
        return "campground/campgroundForm";
    }

    // 폼에서 입력된 캠핑장 정보를 저장
    @PostMapping("/add")
    public String addCampground(@ModelAttribute("campground") CampgroundDto campground,
                                @RequestParam("images")List<MultipartFile> images,
                                @RequestParam("sido") String sido,
                                @RequestParam("sigungu") String sigungu,
                                @RequestParam(value = "subOptionIds", required = false) List<Integer> subOptionIds) {

        CampgroundDto campgroundDto = campgroundService.createCampground(campground, sido, sigungu, subOptionIds, images, filePath);
        return "redirect:/campsite/list?campgroundId=" + campgroundDto.getId();
    }

    @GetMapping
    public String searchCampgrounds(Model model){
        return "campground/campgroundSearch";
    }

    @GetMapping("/list")
    public String getCampgroundList(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "locationDetailId", required = false) Integer locationDetailId,
            @RequestParam(value = "checkInDate") LocalDate checkInDate,
            @RequestParam(value = "checkOutDate") LocalDate checkOutDate,
            Model model) {

        List<CampgroundDto> filteredCampgrounds;

        if(locationDetailId != null) {
            filteredCampgrounds = campgroundService.searchCampgrounds(query, locationDetailId);
        }else{
            // query가 존재하면 이름이나 지역으로 검색
            if (query != null && !query.isEmpty()) {
                filteredCampgrounds = campgroundService.searchCampgrounds(query);
            }
            else{
                filteredCampgrounds = campgroundService.searchAllCampgrounds(); // 쿼리가 없을 경우 모든 캠핑장 목록 반환
            }
        }

        Map<Long, Integer> totalLowestPrices = new HashMap<>();

        // 각 캠프사이트에 대한 총 가격 계산
        for (CampgroundDto campground : filteredCampgrounds) {
            int totalLowestPrice = campgroundService.getLowestPrice(campground, checkInDate, checkOutDate);
            totalLowestPrices.put(campground.getId(), totalLowestPrice);
        }
        filteredCampgrounds = campgroundService.getCampgroundWithImages(filteredCampgrounds);

        model.addAttribute("totalLowestPrices", totalLowestPrices);
        model.addAttribute("campgrounds", filteredCampgrounds);
        return "campground/campgroundList"; // 필터링된 캠핑장을 보여줄 뷰 페이지
    }


    @GetMapping("/edit/{id}")
    public String editCampground(@PathVariable("id") Long id,
                                       @RequestParam(value = "query", required = false) String query,
                                       Model model) {
        // 캠프사이트 정보
        List<CampsiteDto> campsites = campsiteService.findByCampgroundId(id);
        //캠핑장 옵션, 서브 옵션 조회
        List<CampgroundOptionDto> options = campgroundService.getCampgroundOptions();
        List<CampgroundSuboptionDto> suboptions = campgroundService.getCampgroundSuboptions();

        String sido = campgroundService.getLocationSido(id);
        String sigungu = campgroundService.getLocationDetailSigungu(id);

        model.addAttribute("sido", sido);
        model.addAttribute("sigungu", sigungu);

        model.addAttribute("options", options);
        model.addAttribute("suboptions",suboptions);

        model.addAttribute("campground", campgroundService.getCampgroundById(id));
        model.addAttribute("campsites", campsites);

        List<Integer> suboptionIdList= campgroundSuboptionService.getSuboptionId(id);
        model.addAttribute("suboptionIdList", suboptionIdList);

        return "campground/campgroundEdit";
    }

    // 폼에서 입력된 캠핑장 정보를 저장
    @PostMapping("/edit")
    public String editCampground(@ModelAttribute("campground") CampgroundDto campground,
                                @RequestParam("sido") String sido,
                                @RequestParam("sigungu") String sigungu,
                                @RequestParam(value = "subOptionIds", required = false) List<Integer> subOptionIds) {
//        CampgroundDto campgroundDto = campgroundService.createCampground(campground, sido, sigungu, subOptionIds);
        CampgroundDto campgroundDto = campgroundService.editCampground(campground,sido, sigungu, subOptionIds);
        return "redirect:/campsite/list?campgroundId=" + campgroundDto.getId();
    }
}
