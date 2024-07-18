package com.fordogs.user.domain.entity.redis;

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
@RedisHash(value = "emailAuth")
public class EmailAuthCache {

    @Id
    private String authCode;

    private String userAccount;

    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long expirationTime;

    @Builder
    public EmailAuthCache(String authCode, String userAccount, Long expirationTime) {
        this.authCode = authCode;
        this.userAccount = userAccount;
        this.expirationTime = expirationTime;
    }
}
