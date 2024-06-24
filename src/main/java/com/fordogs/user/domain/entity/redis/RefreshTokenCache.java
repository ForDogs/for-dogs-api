package com.fordogs.user.domain.entity.redis;

import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken")
public class RefreshTokenCache {

    @Id
    private String account;

    @Indexed
    private String token;

    @TimeToLive
    private Long expirationTime;

    @Builder
    public RefreshTokenCache(Account account, RefreshToken token, Long expirationTime) {
        this.account = account.getValue();
        this.token = token.getValue();
        this.expirationTime = expirationTime * 60L;
    }
}
