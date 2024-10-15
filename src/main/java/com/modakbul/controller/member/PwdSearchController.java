package com.modakbul.controller.member;

import com.modakbul.entity.member.Member;
import com.modakbul.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PwdSearchController {
    private final MemberService memberService;

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam String email, Model model) {

        model.addAttribute("email", email);
        System.out.println(model);
        System.out.println(email);
        return "resetPassword"; // Thymeleaf 템플릿 이름
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        // 이메일로 회원 조회
        Member member = memberService.findByMail(email);
        System.out.println(member);
        if (member == null) {
            return ResponseEntity.badRequest().body("이메일이 존재하지 않습니다.");
        }

        // 비밀번호 변경 로직
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("비밀번호가 비어있습니다.");
        }
        member.setPassword(newPassword); // 새 비밀번호 설정
        memberService.updateMemberInfo(member, member.getUserName(), newPassword); // 업데이트 메서드 호출

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.\n 확인을 누르시면 로그인 창으로 이동합니다.");
    }
}
