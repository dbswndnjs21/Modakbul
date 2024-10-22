package com.modakbul.service.member;

import com.modakbul.dto.member.HostDto;
import com.modakbul.entity.member.Host;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.member.HostRepository;
import com.modakbul.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HostService {
    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    public void saveHost(Host host) {
//        Host entity = hostDto.toEntity(member);
//        System.out.println("entity = " + entity);
//        System.out.println("호스트서비스");
//        System.out.println(entity.getId());
//        System.out.println(entity.getAccount());

//        System.out.println("member !@!!!");
//        System.out.println(member.getId());
//        System.out.println(member.getUserName());
//        entity.setMember(member);
        hostRepository.save(host);
    }

    // Host 엔티티를 HostDto로 변환하는 메서드
    public HostDto findHostDtoById(Long id) {
        Host host = hostRepository.findById(id).orElseThrow(() -> new RuntimeException("Host not found"));
        return new HostDto(host);  // Host 엔티티를 HostDto로 변환하여 반환
    }
}
