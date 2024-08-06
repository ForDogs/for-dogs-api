package com.fordogs.user.domain.vo;

import com.fordogs.core.domain.vo.ValueObject;
import com.fordogs.core.exception.error.GlobalErrorCode;
import lombok.*;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenMetadata extends ValueObject {

    private Long expirationTime;

    @Builder
    public TokenMetadata(Long expirationTime) {
        validate(expirationTime);
        this.expirationTime = expirationTime;
    }

    private void validate(Long expirationTime) {
        if (expirationTime == null || expirationTime <= 0) {
            throw GlobalErrorCode.internalServerException("유효한 만료 시간이 아닙니다.");
        }
    }
}
