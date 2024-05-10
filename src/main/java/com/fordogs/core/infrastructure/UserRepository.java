package com.fordogs.core.infrastructure;

import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByUserIdentifier(Id userIdentifier);

    Optional<UserEntity> findByUserIdentifier(Id userIdentifier);
}
