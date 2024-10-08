package com.modakbul.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateForm {
    private String name;
    private int totalPrice;
    private String orderNumber;

}
