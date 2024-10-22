package com.modakbul.service.coupon;

import com.modakbul.dto.coupon.CouponDto;
import com.modakbul.entity.coupon.Coupon;
import com.modakbul.repository.coupon.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public List<CouponDto> findCoupons(int userId) {
        List<Coupon> coupon = couponRepository.findCouponById(userId);
        List<CouponDto> couponDto = coupon.stream().map(m -> new CouponDto(m)).toList();
        return couponDto;
    }

    public Coupon findById(int id) {
        return couponRepository.findById(id).orElse(null);
    }
}