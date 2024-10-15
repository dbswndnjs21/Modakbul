package com.modakbul.controller.member;

import com.modakbul.entity.member.Member;
import com.modakbul.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LoginSearchController {

    private final MemberService memberService;
    @GetMapping("/login/search")
    public String search() {
        return "loginSearch";
    }
    @PostMapping("/login/search")
    @ResponseBody // Add this annotation to return a JSON response
    public String search(@RequestParam String username, @RequestParam String mail) {
        Member member = memberService.findByUserNameAndMail(username, mail);
        if (member == null) {
            return "사용자 정보를 찾을 수 없습니다"; // No user found
        } else {
            return "회원님의 ID는: " + member.getUserId() + " 입니다."; // Display the user ID or any member information you want
        }
    }
}
