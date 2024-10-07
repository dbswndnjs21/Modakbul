package com.modakbul.service;

import com.modakbul.entity.member.Member;
import com.modakbul.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(Member member) {
        String password = member.getPassword();
        String encodePassword = passwordEncoder.encode(password); // 암호화
        member.setPassword(encodePassword);
        memberRepository.save(member);
    }


    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }
    public Member findByMail(String mail) { // 추가된 메서드
        return memberRepository.findByMail(mail);
    }
}
