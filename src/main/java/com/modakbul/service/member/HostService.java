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

    // Host 엔티티를 HostDto로 변환하는 메서드
    public HostDto findHostDtoById(Long id) {
        Host host = hostRepository.findById(id).orElseThrow(() -> new RuntimeException("Host not found"));
        return new HostDto(host);  // Host 엔티티를 HostDto로 변환하여 반환
    }
}
