package com.fordogs.user.application;

import com.fordogs.core.domian.entity.RefreshTokenEntity;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.core.infrastructure.RefreshTokenRepository;
import com.fordogs.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshToken createRefreshToken(UserEntity userEntity) {
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(userEntity);
        refreshTokenRepository.save(RefreshTokenEntity.createRefreshTokenEntity(userEntity, refreshToken));

        return refreshToken;
    }
}
