package com.fordogs.user.application;

import com.fordogs.core.util.CookieUtil;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.security.provider.JwtTokenProvider;
import com.fordogs.user.domain.entity.UserManagementEntity;
import com.fordogs.user.domain.vo.wrapper.AccessToken;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.user.presentation.request.UserLoginRequest;
import com.fordogs.user.presentation.response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserAuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRefreshTokenService userRefreshTokenService;
    private final UserManagementService userManagementService;

    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {
        UserManagementEntity userManagementEntity = userManagementService.findByAccount(request.getUserId());

        userManagementEntity.checkRole(request.getUserRole());
        userManagementEntity.validatePassword(request.getUserPassword());
        userManagementEntity.checkIfEnabled();

        RefreshToken refreshToken = userRefreshTokenService.generateAndSaveRefreshToken(userManagementEntity);
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(userManagementEntity);

        HttpServletUtil.addHeaderToResponse("Set-Cookie", CookieUtil.createRefreshTokenCookie(refreshToken));

        return UserLoginResponse.toResponse(userManagementEntity, accessToken);
    }
}
