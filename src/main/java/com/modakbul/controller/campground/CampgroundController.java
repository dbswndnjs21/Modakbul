package com.modakbul.controller.campground;
import com.modakbul.entity.campground.Campground;
import com.modakbul.service.campground.CampgroundService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/campgrounds")
public class CampgroundController {
    private final CampgroundService campgroundService;

    public CampgroundController(CampgroundService campgroundService) {
        this.campgroundService = campgroundService;
    }

    @GetMapping
    public String showCampgroundList(Model model) {
        model.addAttribute("campgrounds", campgroundService.getAllCampgrounds());
        return "campgroundList";
    }

    @GetMapping("/{id}")
    public String showCampgroundDetail(@PathVariable Long id, Model model) {
        model.addAttribute("campground", campgroundService.getCampgroundById(id));
        return "campgroundDetail";
    }
}
