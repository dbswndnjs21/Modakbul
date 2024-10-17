package com.modakbul.service.member;

import com.modakbul.entity.member.Member;
import com.modakbul.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // 사용자 정보 업데이트 메서드 추가
    public void updateMemberInfo(Member member, String username, String password) {
        member.setUserName(username);

        // 비밀번호가 입력되었으면 암호화 후 업데이트
        if (password != null && !password.isEmpty()) {
            String encodedPassword = passwordEncoder.encode(password);
            member.setPassword(encodedPassword);
        }

        memberRepository.save(member);  // 변경된 정보 저장
    }

    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    public Member findByMail(String mail) { // 추가된 메서드
        return memberRepository.findByMail(mail.toLowerCase().trim());
    }

    public Member findByUserNameAndMail(String userName, String mail) { // 이름과 이메일로 아이디 찾기 메서드 추가
        return memberRepository.findByUserNameAndMail(userName, mail.toLowerCase().trim());
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
    public boolean emailExists(String email) {
        return memberRepository.findByMail(email) != null; // 이메일이 존재하면 true 반환
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).get();
    }
    
    public Member findMembership(String userId) {
        return memberRepository.findByUserId(userId);
    }
}
