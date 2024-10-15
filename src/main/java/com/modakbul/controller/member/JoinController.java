package com.modakbul.controller.member;

import com.modakbul.entity.member.Member;
import com.modakbul.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class JoinController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/joinCheck")
    public String joinCheck() {
        return "joinCheck";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/join")
    public String join(Member member) {
        member.setProvider("FORM");
        memberService.save(member);
        return "redirect:/";
    }

    // 아이디 중복 체크 API
    @PostMapping("/checkUserId")
    @ResponseBody
    public String checkUserId(@RequestBody Map<String, String> requestData) {
        String userId = requestData.get("userId");
        Member existingMember = memberService.findByUserId(userId);
        return (existingMember == null) ? "사용 가능" : "이미 사용 중";
    }
    @PostMapping("/checkEmail")
    @ResponseBody
    public String checkEmail(@RequestBody Map<String, String> requestData) {
        String mail = requestData.get("email");
        Member existingMember = memberService.findByMail(mail);
        return (existingMember == null) ? "사용 가능" : "이미 사용 중";
    }
    }

