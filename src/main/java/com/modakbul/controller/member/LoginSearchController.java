package com.modakbul.controller.member;

import com.modakbul.dto.member.MemberDto;
import com.modakbul.entity.member.Member;
import com.modakbul.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginSearchController {

    private final MemberService memberService;

    @GetMapping("/login/search")
    public String search() {
        return "loginSearch";
    }

    @PostMapping("/login/search")
    @ResponseBody
    public ResponseEntity<String> search(@RequestBody MemberDto dto) {
        System.out.println("Received request: " + dto);

        // 사용자 정보를 DB에서 찾기
        Member member = memberService.findByUserNameAndMail(dto.getUserName(), dto.getMail());
        System.out.println("Found member: " + member);

        // 사용자 정보를 찾지 못한 경우
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 정보를 찾을 수 없습니다.");
        } else {
            // 사용자 ID 반환
            return ResponseEntity.ok("회원님의 ID는: " + member.getUserId() + " 입니다.");
        }
    }
}
