package com.modakbul.service;

import com.modakbul.entity.member.Member;
import com.modakbul.repository.MemberRepository;
import com.modakbul.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final HttpSession httpSession;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Member member = null;

        if (registrationId.equals("kakao")) {
            Long id = (Long) attributes.get("id");
            String nickname = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");
            String email = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");

            member = memberRepository.findByUserId(String.valueOf(id));
            if (member == null) {
                member = Member.builder()
                        .userId(String.valueOf(id))
                        .userName(nickname)
                        .mail(email)
                        .role("MEMBER")
                        .provider("KAKAO")
                        .build();
                memberRepository.save(member);
            }
        } else if (registrationId.equals("naver")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            String email = (String) response.get("email");
            String nickname = (String) response.get("name");

            member = memberRepository.findByUserId(email);
            if (member == null) {
                member = Member.builder()
                        .userId(email)
                        .userName(nickname)
                        .mail(email)
                        .role("MEMBER")
                        .provider("NAVER")
                        .build();
                memberRepository.save(member);
            }
        } else if (registrationId.equals("google")) {
            // Google 응답에서 필요한 정보를 가져옵니다.
            String email = (String) attributes.get("email"); // 이메일 정보
            String name = (String) attributes.get("name");   // 이름 정보

            member = memberRepository.findByUserId(email);
            if (member == null) {
                member = Member.builder()
                        .userId(email)
                        .userName(name)
                        .mail(email)
                        .role("MEMBER")
                        .provider("GOOGLE")
                        .build();
                memberRepository.save(member);
            }
        }

        // CustomUsrDetails 객체 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(member, attributes);

        // SecurityContext에 사용자 정보 저장
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new OAuth2AuthenticationToken(
                customUserDetails, // CustomUsrDetails 사용
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")),
                registrationId
        );
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        return customUserDetails; // CustomUsrDetails 반환
    }
}
