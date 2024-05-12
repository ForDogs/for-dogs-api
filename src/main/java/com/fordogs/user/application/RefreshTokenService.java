package com.fordogs.user.application;

import com.fordogs.core.domian.entity.RefreshTokenEntity;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.security.exception.error.JwtErrorCode;
import com.fordogs.core.infrastructure.RefreshTokenRepository;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.security.provider.JwtTokenProvider;
import com.fordogs.user.presentation.dto.RefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN";

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public RefreshToken createRefreshToken(UserEntity userEntity) {
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(userEntity);
        refreshTokenRepository.save(RefreshTokenEntity.createRefreshTokenEntity(userEntity, refreshToken));

        return refreshToken;
    }

    @Transactional
    public RefreshTokenDto.Response refreshAccessToken(String accessToken) {
        if (!jwtTokenProvider.isTokenExpired(accessToken)) {
            throw JwtErrorCode.TOKEN_VALIDITY_REMAINING.toException();
        }
        String refreshToken = HttpServletUtil.getCookie(REFRESH_TOKEN_COOKIE_NAME)
                .orElseThrow(JwtErrorCode.MISSING_REFRESH_TOKEN::toException);
        if (!jwtTokenProvider.compareSubjects(accessToken, refreshToken)) {
            throw JwtErrorCode.TOKEN_ISSUER_MISMATCH.toException();
        }
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(RefreshToken.builder().value(refreshToken).build())
                .orElseThrow(JwtErrorCode.INVALID_REFRESH_TOKEN::toException);
        if (jwtTokenProvider.isTokenExpired(refreshTokenEntity.getToken().getValue())) {
            throw JwtErrorCode.EXPIRED_REFRESH_TOKEN.toException();
        }
        String refreshedAccessToken = jwtTokenProvider.generateAccessToken(refreshTokenEntity.getUser()).getValue();

        return RefreshTokenDto.Response.toResponse(refreshTokenEntity.getUser(), AccessToken.builder().value(refreshedAccessToken).build());
    }
}
