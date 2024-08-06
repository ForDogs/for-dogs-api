package com.fordogs.user.application;

import com.fordogs.core.util.CookieUtil;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.constants.CookieConstants;
import com.fordogs.core.util.crypto.PasswordHasherUtil;
import com.fordogs.security.util.JwtUtil;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.domain.entity.redis.RefreshTokenCache;
import com.fordogs.user.domain.vo.wrapper.AccessToken;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.user.domain.vo.wrapper.UUIDToken;
import com.fordogs.user.error.UserErrorCode;
import com.fordogs.user.infrastructure.UserRepository;
import com.fordogs.user.presentation.request.UserLoginRequest;
import com.fordogs.user.presentation.request.UserSignupRequest;
import com.fordogs.user.presentation.response.UserLoginResponse;
import com.fordogs.user.presentation.response.UserRefreshResponse;
import com.fordogs.user.presentation.response.UserSignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    public UserSignupResponse signupUser(UserSignupRequest request) {
        UserEntity userEntity = request.toEntity();

        if (userRepository.existsByAccount(userEntity.getAccount())) {
            throw UserErrorCode.DUPLICATE_USER_ID.toException();
        }
        if (userRepository.existsByEmail(userEntity.getEmail())) {
            throw UserErrorCode.DUPLICATE_EMAIL.toException();
        }
        UserEntity savedUserEntity = userRepository.save(userEntity);

        return UserSignupResponse.toResponse(savedUserEntity);
    }

    public UserLoginResponse performLogin(UserLoginRequest request) {
        UserEntity userEntity = userQueryService.findByAccount(Account.builder().value(request.getUserId()).build());

        if (!userEntity.getRole().equals(request.getUserRole())) {
            throw UserErrorCode.USER_ROLE_MISMATCH.toException();
        }
        if (!PasswordHasherUtil.matches(request.getUserPassword(), userEntity.getPassword().getValue())) {
            throw UserErrorCode.LOGIN_PASSWORD_FAILED.toException();
        }
        if (!userEntity.isEnabled()) {
            throw UserErrorCode.USER_DISABLED.toException();
        }

        UUIDToken uuidToken = UUIDToken.generate();
        AccessToken accessToken = jwtUtil.generateAccessToken(userEntity, uuidToken.toEncryptedString());
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(userEntity);
        refreshTokenService.saveRefreshToken(userEntity, refreshToken);

        addTokensToResponseHeaders(refreshToken, uuidToken);

        return UserLoginResponse.toResponse(userEntity, accessToken);
    }

    public void performLogout(String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);
        removeTokensFromResponseHeaders();
    }

    public UserRefreshResponse renewAccessToken(String accessToken, String refreshToken, String uuidToken) {
        RefreshTokenCache tokenCache = refreshTokenService.getRefreshToken(refreshToken, accessToken);
        UserEntity userEntity = userQueryService.findByAccount(Account.builder().value(tokenCache.getUserAccount()).build());
        if (!userEntity.isEnabled()) {
            throw UserErrorCode.USER_DISABLED.toException();
        }

        AccessToken newAccessToken = jwtUtil.generateAccessToken(userEntity, UUIDToken.from(uuidToken).toEncryptedString());

        return UserRefreshResponse.toResponse(userEntity, newAccessToken);
    }

    public void deactivateUser() {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity userEntity = userQueryService.findById(userId);
        userEntity.disable();
    }

    private void addTokensToResponseHeaders(RefreshToken refreshToken, UUIDToken uuidToken) {
        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createRefreshTokenCookie(refreshToken));
        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createUUIDTokenCookie(uuidToken, refreshToken.getMetadata().getExpirationTime()));
    }

    private void removeTokensFromResponseHeaders() {
        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createExpiredCookie(CookieConstants.COOKIE_NAME_REFRESH_TOKEN));
        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createExpiredCookie(CookieConstants.COOKIE_NAME_UUID_TOKEN));
    }
}
