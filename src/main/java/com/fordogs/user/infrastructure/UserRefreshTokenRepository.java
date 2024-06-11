package com.fordogs.user.infrastructure;

import com.fordogs.user.domain.entity.UserRefreshTokenEntity;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshTokenEntity, UUID> {

    Optional<UserRefreshTokenEntity> findByToken(RefreshToken token);
}
