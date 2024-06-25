package com.fordogs.user.infrastructure;

import com.fordogs.user.domain.entity.redis.RefreshTokenCache;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenCache, String> {
}
