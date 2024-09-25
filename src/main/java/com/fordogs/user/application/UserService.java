package com.fordogs.user.application;

import com.fordogs.core.util.CookieUtil;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.constants.CookieConstants;
import com.fordogs.core.util.converter.JsonConverter;
import com.fordogs.core.util.crypto.EncryptionUtil;
import com.fordogs.core.util.crypto.PasswordHasherUtil;
import com.fordogs.security.util.JwtUtil;
import com.fordogs.user.domain.enums.Provider;
import com.fordogs.user.domain.vo.AuthCodeData;
import com.fordogs.user.domain.cache.RefreshTokenCache;
import com.fordogs.user.domain.entity.UserEntity;
import com.fordogs.user.domain.vo.wrapper.AccessToken;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.user.domain.vo.wrapper.UUIDToken;
import com.fordogs.user.error.UserErrorCode;
import com.fordogs.user.infrastructure.UserRepository;
import com.fordogs.user.presentation.request.AuthCodeLoginRequest;
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
        if (!userEntity.getProvider().equals(Provider.LOCAL)) {
            throw UserErrorCode.SOCIAL_LOGIN_REQUIRED.toException();
        }
        if (!PasswordHasherUtil.matches(request.getUserPassword(), userEntity.getPassword().getValue())) {
            throw UserErrorCode.LOGIN_PASSWORD_FAILED.toException();
        }
        validateUserStatus(userEntity);

        return generateTokensAndResponse(userEntity);
    }

    public UserLoginResponse performLoginWithAuthCode(AuthCodeLoginRequest request) {
        AuthCodeData authCodeData = JsonConverter.convertJsonToObject(EncryptionUtil.decrypt(request.getAuthCode()), AuthCodeData.class);

        UserEntity userEntity = userQueryService.findById(authCodeData.getUserId());
        if (authCodeData.isAuthCodeExpired()) {
            throw UserErrorCode.AUTH_CODE_EXPIRED.toException();
        }
        if (!authCodeData.getProvider().equals(userEntity.getProvider().name())) {
            throw UserErrorCode.USER_PROVIDER_MISMATCH.toException();
        }
        validateUserStatus(userEntity);

        return generateTokensAndResponse(userEntity);
    }

    public void performLogout(String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);
        removeCookies();
    }

    public UserRefreshResponse renewAccessToken(String accessToken, String refreshToken, String uuidToken) {
        RefreshTokenCache tokenCache = refreshTokenService.getRefreshToken(refreshToken, accessToken);
        UserEntity userEntity = userQueryService.findByAccount(Account.builder().value(tokenCache.getUserAccount()).build());
        validateUserStatus(userEntity);
        AccessToken newAccessToken = jwtUtil.generateAccessToken(userEntity, UUIDToken.from(uuidToken).toEncryptedString());

        return UserRefreshResponse.toResponse(userEntity, newAccessToken);
    }

    public void deactivateUser() {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity userEntity = userQueryService.findById(userId);
        userEntity.disable();
    }

    private void validateUserStatus(UserEntity userEntity) {
        if (!userEntity.isEnabled()) {
            throw UserErrorCode.USER_DISABLED.toException();
        }
    }

    private UserLoginResponse generateTokensAndResponse(UserEntity userEntity) {
        UUIDToken uuidToken = UUIDToken.generate();
        AccessToken accessToken = jwtUtil.generateAccessToken(userEntity, uuidToken.toEncryptedString());
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(userEntity);
        refreshTokenService.saveRefreshToken(userEntity, refreshToken);
        addCookies(uuidToken, refreshToken);

        return UserLoginResponse.toResponse(userEntity, accessToken);
    }

    private void addCookies(UUIDToken uuidToken, RefreshToken refreshToken) {
        HttpServletUtil.addCookieToResponse(CookieUtil.createRefreshTokenCookie(refreshToken));
        HttpServletUtil.addCookieToResponse(CookieUtil.createUUIDTokenCookie(uuidToken, refreshToken.getMetadata().getExpirationTime()));
    }

    private void removeCookies() {
        HttpServletUtil.removeCookieFromResponse(CookieConstants.COOKIE_NAME_REFRESH_TOKEN);
        HttpServletUtil.removeCookieFromResponse(CookieConstants.COOKIE_NAME_UUID_TOKEN);
    }
}