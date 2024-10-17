package com.modakbul.service.coupon;

import com.modakbul.entity.coupon.Coupon;
import com.modakbul.repository.coupon.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public Coupon findCoupons(int userId) {
        return couponRepository.findCouponById(userId);
    }
}