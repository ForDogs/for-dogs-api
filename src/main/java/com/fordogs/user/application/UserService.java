package com.fordogs.user.application;

import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.core.infrastructure.UserRepository;
import com.fordogs.core.util.PasswordUtil;
import com.fordogs.security.provider.JwtTokenProvider;
import com.fordogs.user.error.UserErrorCode;
import com.fordogs.user.presentation.dto.JoinDto;
import com.fordogs.user.presentation.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (userRepository.existsByUserIdentifier(requestedUserEntity.getUserIdentifier())) {
            throw UserErrorCode.DUPLICATE_USER_ID.toException();
        }
        UserEntity savedUserEntity = userRepository.save(requestedUserEntity);

        return JoinDto.Response.toResponse(savedUserEntity);
    }

    @Transactional
    public LoginDto.Response login(LoginDto.Request request) {
        UserEntity userEntity = findUserByIdentifier(request.getUserId());
        if (!(request.getUserRole().equals(userEntity.getRole()))) {
            throw UserErrorCode.USER_ROLE_MISMATCH.toException();
        }
        if (!(PasswordUtil.matches(request.getUserPassword(), userEntity.getPassword().getValue()))) {
            throw UserErrorCode.LOGIN_PASSWORD_FAILED.toException();
        }
        if (userEntity.isDeleted()) {
            throw UserErrorCode.ALREADY_DISABLED.toException();
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEntity);
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(userEntity);

        return LoginDto.Response.toResponse(userEntity, refreshToken, accessToken);
    }

    @Transactional
    public void deactivateUser(String userIdentifier) {
        UserEntity userEntity = findUserByIdentifier(userIdentifier);
        if (userEntity.isDeleted()) {
            throw UserErrorCode.ALREADY_DISABLED.toException();
        }
        userEntity.disableAccount();
    }

    private UserEntity findUserByIdentifier(String userIdentifier) {
        return userRepository.findByUserIdentifier(Id.builder().value(userIdentifier).build())
                .orElseThrow(UserErrorCode.USER_NOT_FOUND::toException);
    }
}
