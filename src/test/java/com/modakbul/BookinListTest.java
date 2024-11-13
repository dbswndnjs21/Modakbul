package com.modakbul;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.modakbul.dto.booking.BookingReservationsDto;
import com.modakbul.entity.booking.Booking;
import com.modakbul.entity.campsite.Campsite;
import com.modakbul.entity.member.Member;
import com.modakbul.repository.booking.BookingRepository;
import com.modakbul.repository.member.MemberRepository;
import com.modakbul.service.booking.BookingService;
import com.modakbul.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

class BookingListTest{
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindBookingList() {
        // given: 사전 조건 설정
        Long memberId = 3L;
        Long campsiteId = 5L; // Campsite의 id 설정

        Member member = new Member(); // Member 객체 생성
        member.setId(memberId); // memberId 설정

        Campsite campsite = new Campsite(); // Campsite 객체 생성
        campsite.setId(campsiteId); // campsiteId 설정

        Booking booking = new Booking(); // Booking 객체 생성
        booking.setMember(member); // Booking에 Member 객체 설정
        booking.setCampsite(campsite); // Booking에 Campsite 객체 설정

        when(bookingRepository.findAllByMemberId(memberId)).thenReturn(List.of(booking)); // BookingRepository에서 반환할 리스트 설정

        // when: 테스트할 동작 수행
        List<BookingReservationsDto> result = bookingService.bookingList(memberId);

        // then: 결과 검증
        assertNotNull(result);
        assertEquals(memberId, result.get(0).getMemberId()); // BookingReservationsDto에 memberId가 있다면 이 방식으로 검증
        assertEquals(campsiteId, result.get(0).getCampsiteId()); // BookingReservationsDto에 campsiteId가 있다면 이 방식으로 검증
    }
}
