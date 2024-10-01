package com.modakbul.controller;

import com.modakbul.entity.member.Member;
import com.modakbul.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {
    @Autowired
    private MemberService memberService;

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
}
