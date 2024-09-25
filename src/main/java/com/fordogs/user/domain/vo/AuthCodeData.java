package com.fordogs.user.domain.vo;

import com.fordogs.core.domain.vo.ValueObject;
import lombok.*;

import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCodeData extends ValueObject {

    private UUID userId;
    private String provider;
    private long authCodeExpirationTime;

    public boolean isAuthCodeExpired() {
        return System.currentTimeMillis() > this.authCodeExpirationTime;
    }
}
