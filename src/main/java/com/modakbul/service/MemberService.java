package com.modakbul.service;

import com.modakbul.entity.Member;
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
        String encodePassword = passwordEncoder.encode(password); // 암호환 한 코드
        member.setPassword(encodePassword);
        memberRepository.save(member);
    }
}
