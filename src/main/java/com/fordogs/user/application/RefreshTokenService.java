package com.fordogs.user.application;

import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import com.fordogs.user.domain.entity.redis.RefreshTokenCache;
import com.fordogs.user.domain.vo.wrapper.AccessToken;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.security.util.JwtUtil;
import com.fordogs.user.error.UserRefreshTokenErrorCode;
import com.fordogs.user.infrastructure.RefreshTokenRepository;
import com.fordogs.user.presentation.response.UserRefreshResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public RefreshToken generateAndSaveRefreshToken(UserManagementEntity userManagementEntity) {
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(userManagementEntity);
        RefreshTokenCache refreshTokenCache = RefreshTokenCache.builder()
                .account(userManagementEntity.getAccount())
                .token(refreshToken)
                .build();

        refreshTokenRepository.save(refreshTokenCache);

        return refreshToken;
    }

    @Transactional
    public UserRefreshResponse refreshAccessToken(String accessToken, String refreshToken) {
        if (!jwtUtil.isTokenExpired(accessToken)) {
            throw UserRefreshTokenErrorCode.TOKEN_VALIDITY_REMAINING.toException();
        }
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw UserRefreshTokenErrorCode.EXPIRED_REFRESH_TOKEN.toException();
        }
        if (!jwtUtil.compareSubjects(accessToken, refreshToken)) {
            throw UserRefreshTokenErrorCode.TOKEN_ISSUER_MISMATCH.toException();
        }
        UserRefreshTokenEntity userRefreshTokenEntity = refreshTokenRepository.findByToken(RefreshToken.builder().value(refreshToken).build())
                .orElseThrow(UserRefreshTokenErrorCode.INVALID_REFRESH_TOKEN::toException);
        userRefreshTokenEntity.getUser().checkIfEnabled();

        AccessToken refreshedAccessToken = jwtUtil.generateAccessToken(userRefreshTokenEntity.getUser());

        return UserRefreshResponse.toResponse(userRefreshTokenEntity.getUser(), refreshedAccessToken);
    }
}
