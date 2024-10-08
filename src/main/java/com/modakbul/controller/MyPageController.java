package com.modakbul.controller;

import com.modakbul.entity.member.Member;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private MemberService memberService;

    @GetMapping("")
    public String myPage(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        } else {
            System.out.println("No valid authentication found.");
            return "redirect:/login";
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
    public String updatePersonalInfo(@AuthenticationPrincipal CustomUserDetails memberDetails,
                                     @RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     Model model) {
        if (memberDetails != null) {
            // DB에서 해당 유저 정보를 가져옴
            Member member = memberService.findByUserId(memberDetails.getUsername());

            // 사용자 정보 업데이트
            memberService.updateMemberInfo(member, username, password);

            // 업데이트 후 로그
            System.out.println("회원 정보 업데이트: " + username);

            // 업데이트 완료 후 마이페이지로 리다이렉트
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
