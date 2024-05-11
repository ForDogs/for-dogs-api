package com.fordogs.user.application;

import com.fordogs.core.domian.entity.RefreshTokenEntity;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.core.infrastructure.RefreshTokenRepository;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.security.exception.JwtException;
import com.fordogs.security.provider.JwtTokenProvider;
import com.fordogs.user.presentation.dto.RefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public RefreshToken createRefreshToken(UserEntity userEntity) {
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(userEntity);
        refreshTokenRepository.save(RefreshTokenEntity.createRefreshTokenEntity(userEntity, refreshToken));

        return refreshToken;
    }

    @Transactional
    public RefreshTokenDto.Response refreshAccessToken(String accessToken) {
        if (!jwtTokenProvider.isTokenExpired(accessToken)) {
            throw new JwtException("AccessToken 유효기간이 남아있습니다.");
        }
        String refreshToken = HttpServletUtil.getCookie("REFRESH_TOKEN")
                .orElseThrow(() -> new JwtException("요청된 쿠키에 'REFRESH_TOKEN' 이름의 쿠키 값이 존재하지 않습니다."));
        if (!jwtTokenProvider.compareSubjects(accessToken, refreshToken)) {
            throw new JwtException("AccessToken과 RefreshToken의 토큰 발급자가 일치하지 않습니다.");
        }
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(RefreshToken.builder().value(refreshToken).build())
                .orElseThrow(() -> new JwtException("쿠키에 저장되어 있는 RefreshToken 값은 유효하지 않은 값입니다."));
        if (jwtTokenProvider.isTokenExpired(refreshTokenEntity.getToken().getValue())) {
            throw new JwtException("RefreshToken 유효기간이 만료되었습니다. 다시 로그인해주세요.");
        }
        String refreshedAccessToken = jwtTokenProvider.generateAccessToken(refreshTokenEntity.getUser()).getValue();

        return RefreshTokenDto.Response.toResponse(refreshTokenEntity.getUser(), AccessToken.builder().value(refreshedAccessToken).build());
    }
}
