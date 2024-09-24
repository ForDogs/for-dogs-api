package com.fordogs.user.application;

import com.fordogs.configuraion.properties.TokenProperties;
import com.fordogs.security.util.JwtUtil;
import com.fordogs.user.domain.entity.UserEntity;
import com.fordogs.user.domain.cache.RefreshTokenCache;
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
    public void saveRefreshToken(UserEntity userEntity, RefreshToken refreshToken) {
        RefreshTokenCache refreshTokenCache = RefreshTokenCache.builder()
                .token(refreshToken)
                .userAccount(userEntity.getAccount())
                .expirationTime((long) tokenProperties.getRefreshTokenExpirationMinutes())
                .build();

        refreshTokenRepository.save(refreshTokenCache);
    }

    @Transactional
    public RefreshTokenCache getRefreshToken(String refreshToken, String accessToken) {
        validateRefreshToken(refreshToken, accessToken);
        return findRefreshToken(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        RefreshTokenCache refreshTokenCache = findRefreshToken(refreshToken);
        refreshTokenRepository.delete(refreshTokenCache);
    }

    private RefreshTokenCache findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findById(refreshToken)
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
