package com.fordogs.user.application;

import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.core.exception.error.UserServiceErrorCode;
import com.fordogs.core.infrastructure.UserRepository;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.PasswordUtil;
import com.fordogs.core.util.constants.RequestAttributesConstants;
import com.fordogs.security.provider.JwtTokenProvider;
import com.fordogs.user.presentation.dto.JoinDto;
import com.fordogs.user.presentation.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public JoinDto.Response joinUser(JoinDto.Request request) {
        UserEntity requestedUserEntity = request.toEntity();
        if (userRepository.existsByAccount(requestedUserEntity.getAccount())) {
            throw UserServiceErrorCode.DUPLICATE_USER_ID.toException();
        }
        UserEntity savedUserEntity = userRepository.save(requestedUserEntity);

        return JoinDto.Response.toResponse(savedUserEntity);
    }

    @Transactional
    public LoginDto.Response login(LoginDto.Request request) {
        UserEntity userEntity = userRepository.findByAccount(Id.builder().value(request.getUserId()).build())
                .orElseThrow(UserServiceErrorCode.USER_NOT_FOUND::toException);
        if (!(request.getUserRole().equals(userEntity.getRole()))) {
            throw UserServiceErrorCode.USER_ROLE_MISMATCH.toException();
        }
        if (!(PasswordUtil.matches(request.getUserPassword(), userEntity.getPassword().getValue()))) {
            throw UserServiceErrorCode.LOGIN_PASSWORD_FAILED.toException();
        }
        if (!userEntity.isEnabled()) {
            throw UserServiceErrorCode.USER_DISABLED.toException();
        }
        RefreshToken refreshToken = refreshTokenService.generateAndSaveRefreshToken(userEntity);
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(userEntity);

        return LoginDto.Response.toResponse(userEntity, refreshToken, accessToken);
    }

    @Transactional
    public void deactivateUser() {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(RequestAttributesConstants.USER_ID);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(UserServiceErrorCode.USER_NOT_FOUND::toException);
        userEntity.disable();
    }
}
