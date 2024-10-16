package com.modakbul.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateForm {
    private String name;
    private int totalPrice;
    private String orderNumber;
    private int bookingId;
}
