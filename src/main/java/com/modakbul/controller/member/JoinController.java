package com.modakbul.controller.member;

import com.modakbul.dto.member.HostDto;
import com.modakbul.entity.member.Host;
import com.modakbul.entity.member.Member;
import com.modakbul.security.CustomUserDetails;
import com.modakbul.service.member.HostService;
import com.modakbul.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller

public class JoinController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private HostService hostService;

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
    @GetMapping("/join/host")
    public String joinHost(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Member member = memberService.findByUserId(userDetails.getUsername()); // 사용자 ID로 멤버 찾기
        model.addAttribute("member", member); // 템플릿에 member 객체 추가
        return "joinHost";
    }
    @PostMapping("/join/host")
    public ResponseEntity<String> joinHost(@AuthenticationPrincipal CustomUserDetails user,
                                           @RequestParam String bankName,
                                           @RequestParam String account,
                                           @RequestParam String accountHolder) {
        // 현재 로그인한 사용자 정보를 가져와 Member 객체를 찾음
        Member member = memberService.findByUserId(user.getUsername());
        member.getId();
        member.getMembership();
        member.getUserName();
        user.getUsername();


        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        // Host 객체 생성 및 필드 설정
//        HostDto host = new HostDto();
//        host.setId(member.getId());
//        host.setBankName(bankName);
//        host.setAccount(account);
//        host.setAccountHolder(accountHolder);
//

        Host host = Host.builder().member(member).bankName(bankName).account(account).accountHolder(accountHolder).build();

        // Host 정보를 저장
        hostService.save(host);
        memberService.saveHost(host.getMember());


        return ResponseEntity.ok("호스트 등록이 완료되었습니다."); // 성공 시 메시지를 포함한 HTTP 200 응답
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

