package com.fordogs.user.application;

import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.entity.UserRefreshTokenEntity;
import com.fordogs.core.domian.vo.wapper.AccessToken;
import com.fordogs.core.domian.vo.wapper.RefreshToken;
import com.fordogs.user.error.UserRefreshTokenErrorCode;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.security.provider.JwtTokenProvider;
import com.fordogs.user.infrastructure.UserRefreshTokenRepository;
import com.fordogs.user.presentation.dto.UserRefreshDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRefreshTokenService {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN";

    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public RefreshToken generateAndSaveRefreshToken(UserManagementEntity userManagementEntity) {
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(userManagementEntity);
        userRefreshTokenRepository.save(UserRefreshTokenEntity.create(userManagementEntity, refreshToken));

        return refreshToken;
    }

    @Transactional
    public UserRefreshDto.Response refreshAccessToken(String accessToken) {
        if (!jwtTokenProvider.isTokenExpired(accessToken)) {
            throw UserRefreshTokenErrorCode.TOKEN_VALIDITY_REMAINING.toException();
        }
        String refreshToken = HttpServletUtil.getCookie(REFRESH_TOKEN_COOKIE_NAME)
                .orElseThrow(UserRefreshTokenErrorCode.MISSING_REFRESH_TOKEN::toException);
        if (jwtTokenProvider.isTokenExpired(refreshToken)) {
            throw UserRefreshTokenErrorCode.EXPIRED_REFRESH_TOKEN.toException();
        }
        UserRefreshTokenEntity userRefreshTokenEntity = userRefreshTokenRepository.findByToken(RefreshToken.builder().value(refreshToken).build())
                .orElseThrow(UserRefreshTokenErrorCode.INVALID_REFRESH_TOKEN::toException);
        if (!userRefreshTokenEntity.getUser().isEnabled()) {
            throw UserRefreshTokenErrorCode.USER_DISABLED.toException();
        }
        if (!jwtTokenProvider.compareSubjects(accessToken, refreshToken)) {
            throw UserRefreshTokenErrorCode.TOKEN_ISSUER_MISMATCH.toException();
        }
        String refreshedAccessToken = jwtTokenProvider.generateAccessToken(userRefreshTokenEntity.getUser()).getValue();

        return UserRefreshDto.Response.toResponse(userRefreshTokenEntity.getUser(), AccessToken.builder().value(refreshedAccessToken).build());
    }
}
