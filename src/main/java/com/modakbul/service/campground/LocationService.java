package com.modakbul.service.campground;

import com.modakbul.entity.campground.Location;
import com.modakbul.repository.campground.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public Location findOnCreateLocation(String sido){
        return locationRepository.findBySido(sido)
                .orElseGet(()->{
                    Location newLocation = new Location();
                    newLocation.setSido(sido);
                    return locationRepository.save(newLocation);
                });
    }
}
