package com.fordogs.payment.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "결제 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCompleteRequest {

    @Schema(description = "포트원 거래 고유 번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "imp_039312484940")
    @NotBlank(message = "포트원 거래 고유 번호를 입력해주세요.")
    private String impUid;

    @Schema(description = "가맹점 주문 번호", requiredMode = Schema.RequiredMode.REQUIRED, example = "11ef1b1c-8e19-e2d5-a95b-e122cba54608")
    @NotNull(message = "가맹점 주문 번호를 입력해주세요.")
    private UUID merchantUid;
}
