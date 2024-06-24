package com.fordogs.user.infrastructure;

import com.fordogs.user.domain.entity.redis.RefreshTokenCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenCache, String> {

    Optional<RefreshTokenCache> findByToken(String refreshToken);
}
