package com.modakbul.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateForm {
    private String name;
    private int totalPrice;

    // 필요에 따라 다른 필드 추가
}
