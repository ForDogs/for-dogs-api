package com.fordogs.user.application;

import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.core.exception.error.UserManagementErrorCode;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.PasswordUtil;
import com.fordogs.core.util.constants.RequestAttributesConstants;
import com.fordogs.security.provider.JwtTokenProvider;
import com.fordogs.user.infrastructure.UserManagementRepository;
import com.fordogs.user.presentation.dto.UserDetailDto;
import com.fordogs.user.presentation.dto.UserJoinDto;
import com.fordogs.user.presentation.dto.UserLoginDto;
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
    public UserJoinDto.Response signupUser(UserJoinDto.Request request) {
        UserManagementEntity requestedUserManagementEntity = request.toEntity();
        if (userManagementRepository.existsByAccount(requestedUserManagementEntity.getAccount())) {
            throw UserManagementErrorCode.DUPLICATE_USER_ID.toException();
        }
        UserManagementEntity savedUserManagementEntity = userManagementRepository.save(requestedUserManagementEntity);

        return UserJoinDto.Response.toResponse(savedUserManagementEntity);
    }

    @Transactional
    public UserLoginDto.Response login(UserLoginDto.Request request) {
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

        return UserLoginDto.Response.toResponse(userManagementEntity, refreshToken, accessToken);
    }

    @Transactional
    public void deactivateUser() {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(RequestAttributesConstants.USER_ID);
        UserManagementEntity userManagementEntity = findById(userId);
        userManagementEntity.disable();
    }

    public UserDetailDto.Response findUserDetails() {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(RequestAttributesConstants.USER_ID);
        UserManagementEntity userManagementEntity = findById(userId);

        return UserDetailDto.Response.toResponse(userManagementEntity);
    }

    public UserManagementEntity findById(UUID userId) {
        return userManagementRepository.findById(userId)
                .orElseThrow(UserManagementErrorCode.USER_NOT_FOUND::toException);
    }
}
