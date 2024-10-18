package com.modakbul.dto.host;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/host")
public class HostController {
    @GetMapping
    public String host() {
        return "host/hostPage";
    }
    @GetMapping("/amount")
    public String amount() {
        return "host/amountPage";
    }
    @GetMapping("/campground")
    public String campground() {
        return "host/campground";
    }
}
