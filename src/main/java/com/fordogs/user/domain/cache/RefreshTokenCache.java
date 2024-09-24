package com.fordogs.user.domain.cache;

import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken")
public class RefreshTokenCache {

    @Id
    private String token;

    private String userAccount;

    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long expirationTime;

    @Builder
    public RefreshTokenCache(RefreshToken token, Account userAccount, Long expirationTime) {
        this.token = token.getValue();
        this.userAccount = userAccount.getValue();
        this.expirationTime = expirationTime;
    }
}
