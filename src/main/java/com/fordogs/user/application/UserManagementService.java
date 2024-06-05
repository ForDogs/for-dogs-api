package com.fordogs.user.application;

import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.vo.wapper.AccessToken;
import com.fordogs.core.domian.vo.wapper.Id;
import com.fordogs.core.domian.vo.wapper.RefreshToken;
import com.fordogs.core.util.CookieUtil;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.PasswordUtil;
import com.fordogs.core.util.constants.HttpRequestConstants;
import com.fordogs.security.provider.JwtTokenProvider;
import com.fordogs.user.error.UserManagementErrorCode;
import com.fordogs.user.infrastructure.UserManagementRepository;
import com.fordogs.user.presentation.request.UserLoginRequest;
import com.fordogs.user.presentation.request.UserSignupRequest;
import com.fordogs.user.presentation.response.UserDetailsResponse;
import com.fordogs.user.presentation.response.UserLoginResponse;
import com.fordogs.user.presentation.response.UserSignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRefreshTokenService userRefreshTokenService;

    @Transactional
    public UserSignupResponse signupUser(UserSignupRequest request) {
        UserManagementEntity requestedUserManagementEntity = request.toEntity();
        if (userManagementRepository.existsByAccount(requestedUserManagementEntity.getAccount())) {
            throw UserManagementErrorCode.DUPLICATE_USER_ID.toException();
        }
        UserManagementEntity savedUserManagementEntity = userManagementRepository.save(requestedUserManagementEntity);

        return UserSignupResponse.toResponse(savedUserManagementEntity);
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {
        UserManagementEntity userManagementEntity = userManagementRepository.findByAccount(Id.builder().value(request.getUserId()).build())
                .orElseThrow(UserManagementErrorCode.USER_NOT_FOUND::toException);
        if (!(request.getUserRole().equals(userManagementEntity.getRole()))) {
            throw UserManagementErrorCode.USER_ROLE_MISMATCH.toException();
        }
        if (!(PasswordUtil.matches(request.getUserPassword(), userManagementEntity.getPassword().getValue()))) {
            throw UserManagementErrorCode.LOGIN_PASSWORD_FAILED.toException();
        }
        if (!userManagementEntity.isEnabled()) {
            throw UserManagementErrorCode.USER_DISABLED.toException();
        }
        RefreshToken refreshToken = userRefreshTokenService.generateAndSaveRefreshToken(userManagementEntity);
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(userManagementEntity);

        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createRefreshTokenCookie(refreshToken));

        return UserLoginResponse.toResponse(userManagementEntity, accessToken);
    }

    @Transactional
    public void deactivateUser() {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(HttpRequestConstants.REQUEST_ATTRIBUTE_USER_ID);
        UserManagementEntity userManagementEntity = findById(userId);
        userManagementEntity.disable();
    }

    public UserDetailsResponse findUserDetails() {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(HttpRequestConstants.REQUEST_ATTRIBUTE_USER_ID);
        UserManagementEntity userManagementEntity = findById(userId);

        return UserDetailsResponse.toResponse(userManagementEntity);
    }

    public UserManagementEntity findById(UUID userId) {
        return userManagementRepository.findById(userId)
                .orElseThrow(UserManagementErrorCode.USER_NOT_FOUND::toException);
    }
}
