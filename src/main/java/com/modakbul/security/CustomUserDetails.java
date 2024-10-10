package com.modakbul.security;

import com.modakbul.entity.member.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomUserDetails implements UserDetails, OAuth2User {

    @Getter
    private Member member;
    private Map<String, Object> attributes;  // OAuth2User의 속성

    // 기본 생성자
    public CustomUserDetails(Member member) {
        this.member = member;
    }

    // OAuth2 사용자 정보를 처리하는 생성자
    public CustomUserDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUserId();
    }
    
    public Long getId() {
    	return member.getId();
    }
    
    public String getUserName() {
        return member.getUserName();
    }


    public Member getMember() {
        return member;
    }

    // OAuth2User 메서드 구현
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return member.getUserName(); // OAuth2User의 getName()은 회원 이름 반환
    }


}
