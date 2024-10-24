package com.modakbul.controller.help;

import com.modakbul.security.CustomUserDetails;
import org.hibernate.annotations.Cache;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelpController {
    @GetMapping("/help")
    public String help(@AuthenticationPrincipal CustomUserDetails member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
            model.addAttribute("role", member.getRole());
        } else {
            model.addAttribute("member", null); // 혹은 다른 값으로 처리 가능
            model.addAttribute("role", null); // 역할이 없는 경우 처리
        }
        return "help/help";
    }
}
