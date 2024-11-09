package com.modakbul.service.campground;

import com.modakbul.dto.campground.LocationDetailDto;
import com.modakbul.dto.campground.LocationDto;
import com.modakbul.entity.campground.Location;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.repository.campground.LocationDetailRepository;
import com.modakbul.repository.campground.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationDetailService {
    @Autowired
    private LocationDetailRepository locationDetailRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationRepository locationRepository;

    public LocationDetailDto findOrCreateLocationDetail(String sigungu, LocationDto locationDto){
        Location location = locationDto.toEntity();
        LocationDetail locationDetail = locationDetailRepository.findBySigunguAndLocation(sigungu, locationDto.toEntity())
                .orElseGet(()->{
                    LocationDetail newLocationDetail = new LocationDetail();
                    newLocationDetail.setSigungu(sigungu);
                    newLocationDetail.setLocation(location);
                    return locationDetailRepository.save(newLocationDetail);
                });

        LocationDetailDto locationDetailDto = new LocationDetailDto(locationDetail);
        return locationDetailDto;
    }
}
