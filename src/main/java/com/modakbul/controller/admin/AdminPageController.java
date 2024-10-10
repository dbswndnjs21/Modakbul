package com.modakbul.controller.admin;

import com.modakbul.entity.member.Member;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.DELETE;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
    @Autowired
    private MemberService memberService;
    @GetMapping
    public String adminPage(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        List<Member> members = memberService.findAllMembers();
        model.addAttribute("member", member);
        model.addAttribute("members", members);
        return "admin/adminPage";
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin"; // 삭제 후 관리자 페이지로 리디렉션
    }


}