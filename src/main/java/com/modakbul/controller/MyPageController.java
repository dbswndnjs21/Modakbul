package com.modakbul.controller;

import com.modakbul.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @GetMapping("")
    public String myPage(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        } else {
            System.out.println("No valid authentication found.");
        }
        return "mypage/myPage";
    }

    @GetMapping("/reservations")
    public String reservations(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        } else {
            System.out.println("No valid authentication found.");
        }
        return "mypage/reservations";
    }

    // 개인정보 수정 폼을 불러오는 메서드
    @GetMapping("/personalInfo")
    public String personalInfo(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        } else {
            System.out.println("No valid authentication found.");
            return "redirect:/login"; // 인증되지 않은 경우 로그인 페이지로 리다이렉트
        }
        return "mypage/personalInfo"; // 개인 정보 수정 폼 뷰 반환
    }

    @PostMapping("/updateInfo")
    public String updatePersonalInfo(@AuthenticationPrincipal CustomUserDetails member,
                                     @RequestParam("username") String username,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     Model model) {
        if (member != null) {
            // 사용자 정보 업데이트 로직 추가
            System.out.println("회원 정보 업데이트: " + username + ", " + email);
            // 업데이트가 완료되면 마이페이지로 리다이렉트
            return "redirect:/mypage/myPage";
        }
        return "redirect:/login";
    }
    @GetMapping("/reviews")
    public String rewviews(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        return "mypage/reviews";
    }
    @GetMapping("/payments")
    public String payments(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        return "mypage/payments";
    }
}
