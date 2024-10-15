package com.modakbul.service.campground;

import com.modakbul.entity.campground.Location;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.repository.campground.LocationDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationDetailService {
    @Autowired
    private LocationDetailRepository locationDetailRepository;

    public LocationDetail findOrCreateLocationDetail(String sigungu, Location location){
        return locationDetailRepository.findBySigunguAndLocation(sigungu, location)
                .orElseGet(()->{
                    LocationDetail newLocationDetail = new LocationDetail();
                    newLocationDetail.setSigungu(sigungu);
                    newLocationDetail.setLocation(location);
                    return locationDetailRepository.save(newLocationDetail);
                });
    }
}
