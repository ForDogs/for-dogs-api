package com.fordogs.user.application;

import com.fordogs.configuraion.properties.TokenProperties;
import com.fordogs.security.util.JwtUtil;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import com.fordogs.user.domain.entity.redis.RefreshTokenCache;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.user.error.RefreshTokenErrorCode;
import com.fordogs.user.infrastructure.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProperties tokenProperties;
    private final JwtUtil jwtUtil;

    @Transactional
    public RefreshToken generateAndSaveRefreshToken(UserManagementEntity userManagementEntity) {
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(userManagementEntity);
        RefreshTokenCache refreshTokenCache = RefreshTokenCache.builder()
                .account(userManagementEntity.getAccount())
                .token(refreshToken)
                .expirationTime((long) tokenProperties.getRefreshTokenExpirationDays())
                .build();

        refreshTokenRepository.save(refreshTokenCache);

        return refreshToken;
    }

    @Transactional
    public RefreshTokenCache getRefreshTokenCache(String refreshToken, String accessToken) {
        validateRefreshToken(refreshToken, accessToken);
        return findRefreshTokenCache(refreshToken);
    }

    @Transactional
    public void deleteRefreshTokenCache(String refreshToken) {
        RefreshTokenCache refreshTokenCache = findRefreshTokenCache(refreshToken);
        refreshTokenRepository.delete(refreshTokenCache);
    }

    private RefreshTokenCache findRefreshTokenCache(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(RefreshTokenErrorCode.INVALID_REFRESH_TOKEN::toException);
    }

    private void validateRefreshToken(String refreshToken, String accessToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw RefreshTokenErrorCode.EXPIRED_REFRESH_TOKEN.toException();
        }
        if (!jwtUtil.compareSubjects(accessToken, refreshToken)) {
            throw RefreshTokenErrorCode.TOKEN_ISSUER_MISMATCH.toException();
        }
    }
}
