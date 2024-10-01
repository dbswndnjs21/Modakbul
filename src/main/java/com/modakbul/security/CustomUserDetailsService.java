package com.modakbul.security;

import com.modakbul.entity.member.Member;
import com.modakbul.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println("test :" + userId);
        Member member = memberRepository.findByUserId(userId);
        if (member == null) {
            throw new UsernameNotFoundException("User not found"); //
        }
        UserDetails userDetails = new CustomUserDetails(member);
        // 바로 인터페이스라서 생성못해서 UserDetails를 구현한CustomUsrDetails로 생성
        return userDetails;
    }
}
