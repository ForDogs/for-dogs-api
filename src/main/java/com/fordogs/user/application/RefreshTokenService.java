package com.fordogs.user.application;

import com.fordogs.core.domian.entity.RefreshTokenEntity;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.core.exception.error.RefreshTokenServiceErrorCode;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken generateAndSaveRefreshToken(UserEntity userEntity) {
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(userEntity);
        refreshTokenRepository.save(RefreshTokenEntity.create(userEntity, refreshToken));

        return refreshToken;
    }

    @Transactional
    public RefreshTokenDto.Response refreshAccessToken(String accessToken) {
        if (!jwtTokenProvider.isTokenExpired(accessToken)) {
            throw RefreshTokenServiceErrorCode.TOKEN_VALIDITY_REMAINING.toException();
        }
        String refreshToken = HttpServletUtil.getCookie(REFRESH_TOKEN_COOKIE_NAME)
                .orElseThrow(RefreshTokenServiceErrorCode.MISSING_REFRESH_TOKEN::toException);
        if (jwtTokenProvider.isTokenExpired(refreshToken)) {
            throw RefreshTokenServiceErrorCode.EXPIRED_REFRESH_TOKEN.toException();
        }
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(RefreshToken.builder().value(refreshToken).build())
                .orElseThrow(RefreshTokenServiceErrorCode.INVALID_REFRESH_TOKEN::toException);
        if (!refreshTokenEntity.getUser().isEnabled()) {
            throw RefreshTokenServiceErrorCode.USER_DISABLED.toException();
        }
        if (!jwtTokenProvider.compareSubjects(accessToken, refreshToken)) {
            throw RefreshTokenServiceErrorCode.TOKEN_ISSUER_MISMATCH.toException();
        }
        String refreshedAccessToken = jwtTokenProvider.generateAccessToken(refreshTokenEntity.getUser()).getValue();

        return RefreshTokenDto.Response.toResponse(refreshTokenEntity.getUser(), AccessToken.builder().value(refreshedAccessToken).build());
    }
}
