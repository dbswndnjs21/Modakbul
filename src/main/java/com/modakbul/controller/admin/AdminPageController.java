package com.modakbul.controller.admin;

import com.modakbul.dto.campground.CampgroundDto;
import com.modakbul.dto.freeboard.FreeboardCommentDto;
import com.modakbul.entity.campground.Campground;
import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.member.Member;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.freeboard.FreeboardCommentService;
import com.modakbul.service.member.MemberService;
import com.modakbul.service.campground.CampgroundService;
import com.modakbul.service.freeboard.FreeboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @Autowired
    private FreeboardCommentService freeboardCommentService;

    @ModelAttribute
    public void addAttributes(Model model){
        List<Member> members = memberService.findAllMembers();
        List<CampgroundDto> camp = campgroundService.getAllCampgrounds();
        List<Freeboard> freeboard = freeboardService.getAllPosts();
        List<FreeboardCommentDto> comments = freeboardCommentService.findAllComments(); // 댓글 목록 가져오기


        model.addAttribute("members", members);
        model.addAttribute("camp", camp);
        model.addAttribute("freeboard", freeboard);
        model.addAttribute("comments", comments); // 댓글 목록 추가
        model.addAttribute("today", LocalDate.now()); // 오늘 날짜
        model.addAttribute("tomorrow", LocalDate.now().plusDays(1)); // 내일 날짜
    }

    @GetMapping
    public String adminPage(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        model.addAttribute("member", member);
        return "admin/adminPage";
    }
    @GetMapping("/member")
    public String memberPage(){
        return "admin/member";
    }
    @GetMapping("/campGround")
    public String campGround(){
        return "admin/campground";
    }
    @GetMapping("/freeBoard")
    public String freeBoard(){
        return "admin/freeBoard";
    }
    @GetMapping("/comments")
    public String comments(){
        return "admin/comments";
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
    @PostMapping("/approve/{id}")
    public String approveCampground(@PathVariable Long id) {
        campgroundService.approveCampground(id);  // 캠핑장 승인 처리
        return "redirect:/admin";  // 처리 결과 반환
    }
    @PostMapping("/approveCancel/{id}")
    public String disApproveCampground(@PathVariable Long id) {
        campgroundService.disApproveCampgorund(id);  // 캠핑장 승인 처리
        return "redirect:/admin";  // 처리 결과 반환
    }
}