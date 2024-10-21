package com.modakbul.utils;

import com.modakbul.entity.campground.CampgroundOption;
import com.modakbul.entity.campground.CampgroundSuboption;
import com.modakbul.repository.campground.CampgroundOptionRepository;
import com.modakbul.repository.campground.CampgroundSuboptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final CampgroundOptionRepository campgroundOptionRepository;
    private final CampgroundSuboptionRepository campgroundSuboptionRepository;
    public DataLoader(CampgroundOptionRepository campgroundOptionRepository, CampgroundSuboptionRepository campgroundSuboptionRepository) {
        this.campgroundOptionRepository = campgroundOptionRepository;
        this.campgroundSuboptionRepository = campgroundSuboptionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        addCampgroundOption();
        addCampgroundSuboptions();
        addCampsiteOption();
        addCampsiteSubOption();
    }

    @Transactional // 모든 삽입 작업을 하나의 트랜잭션으로 묶음
    public void addCampgroundOption(){
        List<String> options = Arrays.asList(
                "캠핑유형",
                "환경",
                "입지형태",
                "기본시설",
                "부가시설",
                "레저",
                "주차형태",
                "숙박일수"
        );

        // 각 옵션에 대해 중복 여부를 확인하고 삽입
        for (String optionName : options) {
            if (campgroundOptionRepository.findByOptionName(optionName) == null) {
                CampgroundOption option = new CampgroundOption();
                option.setOptionName(optionName);
                campgroundOptionRepository.save(option);
            }
        }

    }
    public void addCampgroundSuboptions(){
        CampgroundOption campType = campgroundOptionRepository.findByOptionName("캠핑유형");
        CampgroundOption enviroment = campgroundOptionRepository.findByOptionName("환경");
        CampgroundOption basicFacilitie = campgroundOptionRepository.findByOptionName("기본시설");
        CampgroundOption additionalFacilitie = campgroundOptionRepository.findByOptionName("부가시설");
        CampgroundOption leisure = campgroundOptionRepository.findByOptionName("레저");
        CampgroundOption parkingType = campgroundOptionRepository.findByOptionName("주차형태");
        CampgroundOption numberOfStays = campgroundOptionRepository.findByOptionName("숙박일수");

        addCampgroundSuboption(campType.getId(), Arrays.asList(
                "오토캠핑", "글램핑", "카라반", "팬션", "방갈로", "차박"
        ));

        addCampgroundSuboption(enviroment.getId(), Arrays.asList(
                "바다", "산", "숲", "강", "호수", "계곡", "섬", "평야", "기타"
        ));

        addCampgroundSuboption(basicFacilitie.getId(), Arrays.asList(
                "파쇄석", "데크", "잔디", "마사토", "모래", "자갈", "혼합", "기타"
        ));

        addCampgroundSuboption(additionalFacilitie.getId(), Arrays.asList(
                "화장실", "샤워실", "개별샤워실", "개수대", "매점", "카페", "와이파이", "전기차충전소", "바베큐장"
        ));

        addCampgroundSuboption(leisure.getId(), Arrays.asList(
                "놀이시설", "체험활동", "수영장", "트램펄린", "산책로", "장비대여", "반려동물",
                "트레일러진입", "카라반진입", "찜질방", "짚라인", "온수수영장", "썰매장",
                "농장체험", "동물체험", "장비보관"
        ));

        addCampgroundSuboption(parkingType.getId(), Arrays.asList(
                "텐트 옆 주차", "주차장 주차"
        ));

        addCampgroundSuboption(numberOfStays.getId(), Arrays.asList(
                "캠프닉", "1박이상"
        ));
    }

    public void addCampgroundSuboption(int optionId, List<String> subOptions){
        for (String subOptionName : subOptions) {
            if (campgroundSuboptionRepository.findByOptionNameAndCampgroundOptionId(subOptionName, optionId) == null) {
                CampgroundSuboption subOption = new CampgroundSuboption();
                subOption.setOptionName(subOptionName);
                subOption.setCampgroundOption(campgroundOptionRepository.findById(optionId).orElse(null)); // 해당 옵션에 연결
                campgroundSuboptionRepository.save(subOption);
            }
        }
    }

    public void addCampsiteOption(){

    }

    public void addCampsiteSubOption(){

    }
}
