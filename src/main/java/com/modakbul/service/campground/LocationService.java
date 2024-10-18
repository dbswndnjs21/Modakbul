package com.modakbul.service.campground;

import com.modakbul.entity.campground.Location;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.repository.campground.LocationDetailRepository;
import com.modakbul.repository.campground.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationDetailRepository locationDetailRepository;

    public Location findOnCreateLocation(String sido){
        return locationRepository.findBySido(sido)
                .orElseGet(()->{
                    Location newLocation = new Location();
                    newLocation.setSido(sido);
                    return locationRepository.save(newLocation);
                });
    }

    public List<LocationDetail> getLocationDetailsByLocationId(int locationId) {
        return locationDetailRepository.findByLocationId(locationId);
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }
}
