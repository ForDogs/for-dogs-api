package com.fordogs.payment.application.portone.response;

import lombok.*;


@Getter
@Setter
@ToString(callSuper = true)
public class PaymentBaseResponse {

    private Integer code;

    private String message;
}
