package com.fordogs.user.application;

import com.fordogs.core.util.cookie.CookieUtil;
import com.fordogs.core.util.http.HttpServletUtil;
import com.fordogs.core.util.constants.CookieConstants;
import com.fordogs.security.util.JwtUtil;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import com.fordogs.user.domain.entity.redis.RefreshTokenCache;
import com.fordogs.user.domain.vo.wrapper.AccessToken;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.user.domain.vo.wrapper.UUIDToken;
import com.fordogs.user.error.UserManagementErrorCode;
import com.fordogs.user.infrastructure.UserManagementRepository;
import com.fordogs.user.presentation.request.UserLoginRequest;
import com.fordogs.user.presentation.request.UserSignupRequest;
import com.fordogs.user.presentation.response.UserDetailsResponse;
import com.fordogs.user.presentation.response.UserLoginResponse;
import com.fordogs.user.presentation.response.UserRefreshResponse;
import com.fordogs.user.presentation.response.UserSignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserSignupResponse signupUser(UserSignupRequest request) {
        UserManagementEntity userManagementEntity = request.toEntity();

        if (userManagementRepository.existsByAccount(userManagementEntity.getAccount())) {
            throw UserManagementErrorCode.DUPLICATE_USER_ID.toException();
        }
        UserManagementEntity savedUserManagementEntity = userManagementRepository.save(userManagementEntity);

        return UserSignupResponse.toResponse(savedUserManagementEntity);
    }

    @Transactional
    public UserLoginResponse performLogin(UserLoginRequest request) {
        UserManagementEntity userManagementEntity = findByAccount(Account.builder().value(request.getUserId()).build());

        userManagementEntity.validateRole(request.getUserRole());
        userManagementEntity.validatePassword(request.getUserPassword());
        userManagementEntity.validateIfEnabled();

        UUIDToken uuidToken = UUIDToken.generate();
        AccessToken accessToken = jwtUtil.generateAccessToken(userManagementEntity, uuidToken.toEncryptedString());
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(userManagementEntity);
        refreshTokenService.saveRefreshToken(userManagementEntity, refreshToken);

        addTokensToResponseHeaders(refreshToken, uuidToken);

        return UserLoginResponse.toResponse(userManagementEntity, accessToken);
    }

    public void performLogout(String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);
        removeTokensFromResponseHeaders();
    }

    private void addTokensToResponseHeaders(RefreshToken refreshToken, UUIDToken uuidToken) {
        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createRefreshTokenCookie(refreshToken));
        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createUUIDTokenCookie(uuidToken, refreshToken.getExpirationTime()));
    }

    private void removeTokensFromResponseHeaders() {
        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createExpiredCookie(CookieConstants.COOKIE_NAME_REFRESH_TOKEN));
        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createExpiredCookie(CookieConstants.COOKIE_NAME_UUID_TOKEN));
    }

    @Transactional
    public UserRefreshResponse renewAccessToken(String accessToken, String refreshToken, String uuidToken) {
        RefreshTokenCache tokenCache = refreshTokenService.getRefreshToken(refreshToken, accessToken);
        UserManagementEntity userManagementEntity = findByAccount(Account.builder().value(tokenCache.getUserAccount()).build());
        userManagementEntity.validateIfEnabled();

        AccessToken newAccessToken = jwtUtil.generateAccessToken(userManagementEntity, UUIDToken.from(uuidToken).toEncryptedString());

        return UserRefreshResponse.toResponse(userManagementEntity, newAccessToken);
    }

    @Transactional
    public void deactivateUser() {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserManagementEntity userManagementEntity = findById(userId);
        userManagementEntity.disable();
    }

    public UserDetailsResponse findUserDetails() {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserManagementEntity userManagementEntity = findById(userId);

        return UserDetailsResponse.toResponse(userManagementEntity);
    }

    public UserManagementEntity findById(UUID userId) {
        return userManagementRepository.findById(userId)
                .orElseThrow(UserManagementErrorCode.USER_NOT_FOUND::toException);
    }

    public UserManagementEntity findByAccount(Account account) {
        return userManagementRepository.findByAccount(account)
                .orElseThrow(UserManagementErrorCode.USER_NOT_FOUND::toException);
    }
}
