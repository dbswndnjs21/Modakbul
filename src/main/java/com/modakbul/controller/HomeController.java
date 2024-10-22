package com.modakbul.controller;

import com.modakbul.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails member, Model model) {

        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("role", member.getRole());
            model.addAttribute("membership", member.getMembership());

            System.out.println("UserDetails member: " + member.getUserName());
        } else {
            System.out.println("No valid authentication found.");
        }

//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // 인증 정보가 있는지 확인
//        if (authentication != null) {
//            System.out.println("Authentication: " + authentication);
//            System.out.println("Principal: " + authentication.getPrincipal());
//            System.out.println("Authorities: " + authentication.getAuthorities());
//            System.out.println("Details: " + authentication.getDetails());
//        } else {
//            System.out.println("No authentication information found.");
//        }
        return "home";
    }
}

