package com.fordogs.user.infrastructure;

import com.fordogs.core.domian.entity.UserRefreshTokenEntity;
import com.fordogs.core.domian.vo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshTokenEntity, UUID> {

    Optional<UserRefreshTokenEntity> findByToken(RefreshToken token);
}
