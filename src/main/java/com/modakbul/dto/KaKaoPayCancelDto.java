package com.modakbul.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class KaKaoPayCancelDto {
    private String aid;
    private String tid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;

    private Amount amount;
    private Amount approved_cancel_amount;
    private Amount canceled_amount;
    private Amount cancel_available_amount;

    private String item_name;
    private int quantity; // 상품 수량
    private String created_at; // 결제 준비 요청 시각
    private String approved_at; // 결제 승인 시각
    private String canceled_at; // 결제 취소 시각
    private String payload; // 취소 요청 시 전달한 값

    @Getter
    @Setter
    @ToString
    public class Amount {
        public int total;
        public int tax_free;
        public int vat;
        public int point;
    }

}