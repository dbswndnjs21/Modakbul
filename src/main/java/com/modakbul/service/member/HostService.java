package com.modakbul.service.member;

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

    public void save(Host host){
        memberRepository.save(host.getMember());
        hostRepository.save(host);

    }

    public Host findById(Long id) {
        return hostRepository.findById(id).orElse(null);
    }
}
