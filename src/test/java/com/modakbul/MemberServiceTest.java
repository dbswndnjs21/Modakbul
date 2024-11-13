package com.modakbul;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.modakbul.entity.member.Member;
import com.modakbul.repository.member.MemberRepository;
import com.modakbul.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindMemberById() {
        // given: 사전 조건 설정
        String userId = "";
        Member mockMember = new Member(userId);
        when(memberRepository.findByUserId(userId)).thenReturn(mockMember);

        // when: 테스트할 동작 수행
        Member result = memberService.findByUserId(userId);

        // then: 결과 검증
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }
    
}
