package com.fordogs.user.infrastructure;

import com.fordogs.user.domain.entity.redis.EmailAuthCache;
import org.springframework.data.repository.CrudRepository;

public interface EmailAuthRepository extends CrudRepository<EmailAuthCache, String> {
}
