package com.modakbul.service.member;

import com.modakbul.entity.member.Host;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.member.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HostService {
    @Autowired
    private HostRepository hostRepository;

    public Host findById(Long id) {
        return hostRepository.findById(id).orElse(null);
    }
}
