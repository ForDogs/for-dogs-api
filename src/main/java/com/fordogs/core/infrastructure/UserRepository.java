package com.fordogs.core.infrastructure;

import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.user.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByUserId(Id userId);
}
