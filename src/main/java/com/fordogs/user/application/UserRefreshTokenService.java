package com.fordogs.user.application;

import com.fordogs.user.domain.entity.UserManagementEntity;
import com.fordogs.user.domain.entity.UserRefreshTokenEntity;
import com.fordogs.user.domain.vo.wrapper.AccessToken;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.security.provider.JwtTokenProvider;
import com.fordogs.user.error.UserRefreshTokenErrorCode;
import com.fordogs.user.infrastructure.UserRefreshTokenRepository;
import com.fordogs.user.presentation.response.UserRefreshResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRefreshTokenService {

    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public RefreshToken generateAndSaveRefreshToken(UserManagementEntity userManagementEntity) {
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(userManagementEntity);
        userRefreshTokenRepository.save(UserRefreshTokenEntity.create(userManagementEntity, refreshToken));

        return refreshToken;
    }

    @Transactional
    public UserRefreshResponse refreshAccessToken(String accessToken, String refreshToken) {
        validateTokens(accessToken, refreshToken);
        UserRefreshTokenEntity userRefreshTokenEntity = userRefreshTokenRepository.findByToken(RefreshToken.builder().value(refreshToken).build())
                .orElseThrow(UserRefreshTokenErrorCode.INVALID_REFRESH_TOKEN::toException);
        userRefreshTokenEntity.getUser().checkIfEnabled();

        AccessToken refreshedAccessToken = jwtTokenProvider.generateAccessToken(userRefreshTokenEntity.getUser());

        return UserRefreshResponse.toResponse(userRefreshTokenEntity.getUser(), refreshedAccessToken);
    }

    private void validateTokens(String accessToken, String refreshToken) {
        if (!jwtTokenProvider.isTokenExpired(accessToken)) {
            throw UserRefreshTokenErrorCode.TOKEN_VALIDITY_REMAINING.toException();
        }
        if (jwtTokenProvider.isTokenExpired(refreshToken)) {
            throw UserRefreshTokenErrorCode.EXPIRED_REFRESH_TOKEN.toException();
        }
        if (!jwtTokenProvider.compareSubjects(accessToken, refreshToken)) {
            throw UserRefreshTokenErrorCode.TOKEN_ISSUER_MISMATCH.toException();
        }
    }
}
