package com.modakbul.service.campground;

import com.modakbul.dto.campground.LocationDetailDto;
import com.modakbul.dto.campground.LocationDto;
import com.modakbul.entity.campground.Location;
import com.modakbul.entity.campground.LocationDetail;
import com.modakbul.repository.campground.LocationDetailRepository;
import com.modakbul.repository.campground.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationDetailRepository locationDetailRepository;

    public LocationDto findOnCreateLocation(String sido){
        Location location = locationRepository.findBySido(sido)
                .orElseGet(()->{
                    Location newLocation = new Location();
                    newLocation.setSido(sido);
                    return locationRepository.save(newLocation);
                });
        LocationDto locationDto = new LocationDto(location);
        return locationDto;
    }

    public List<LocationDetailDto> getLocationDetailsByLocationId(int locationId) {
        List<LocationDetail> locationDetails = locationDetailRepository.findByLocationId(locationId);

        List<LocationDetailDto> locationDetailDtos = locationDetails.stream()
                .map(locationDetail -> new LocationDetailDto(locationDetail))
                .collect(Collectors.toList());
        return locationDetailDtos;
    }

    public List<LocationDto> findAll() {
        List<Location> locations = locationRepository.findAll();

        List<LocationDto> locationDtos = locations.stream()
                .map(location -> new LocationDto(location))
                .collect(Collectors.toList());

        return locationDtos;
    }

    public Location findById(int id) {
        return locationRepository.findById(id);
    }
}
