package com.fordogs.core.infrastructure;

import com.fordogs.core.domian.entity.RefreshTokenEntity;
import com.fordogs.core.domian.vo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByToken(RefreshToken token);
}
