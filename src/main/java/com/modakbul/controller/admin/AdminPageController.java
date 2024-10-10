package com.modakbul.controller.admin;

import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.member.Member;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.MemberService;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.freeboard.FreeboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private CampgroundService campgroundService;
    @Autowired
    private FreeboardService freeboardService;

    @GetMapping
    public String adminPage(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        List<Member> members = memberService.findAllMembers();
        List<Campground> camp = campgroundService.getAllCampgrounds();
        List<Freeboard> freeboard = freeboardService.findAllWithImages();
        model.addAttribute("member", member);
        model.addAttribute("members", members);
        model.addAttribute("camp", camp);
        model.addAttribute("freeboard", freeboard);
        return "admin/adminPage";
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin"; // 삭제 후 관리자 페이지로 리디렉션
    }
    @DeleteMapping("/freeboard/delete/{id}")
    public String deleteFreeboard(@PathVariable Long id) {
        freeboardService.deleteBoard(id,null); // Freeboard 삭제 서비스 호출
        return "redirect:/admin"; // 삭제 후 관리자 페이지로 리디렉션
    }



}