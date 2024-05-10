package com.fordogs.user.application;

import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.core.infrastructure.UserRepository;
import com.fordogs.core.util.PasswordUtil;
import com.fordogs.security.util.JwtTokenProvider;
import com.fordogs.user.error.UserErrorCode;
import com.fordogs.user.presentation.dto.JoinDto;
import com.fordogs.user.presentation.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
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
        UserEntity userEntity = userRepository.findByUserIdentifier(Id.builder().value(request.getUserId()).build())
                .orElseThrow(UserErrorCode.LOGIN_ID_FAILED::toException);
        if (!(PasswordUtil.matches(request.getPassword(), userEntity.getPassword().getValue()))) {
            throw UserErrorCode.LOGIN_PASSWORD_FAILED.toException();
        }
        if (userEntity.isDeleted()) {
            throw UserErrorCode.USER_DISABLED.toException();
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword());
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEntity);
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(userEntity);

        return LoginDto.Response.toResponse(userEntity, refreshToken, accessToken);
    }
}
