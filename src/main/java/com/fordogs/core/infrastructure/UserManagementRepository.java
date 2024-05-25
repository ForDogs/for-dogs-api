package com.fordogs.core.infrastructure;

import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.vo.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserManagementRepository extends JpaRepository<UserManagementEntity, UUID> {

    boolean existsByAccount(Id account);

    Optional<UserManagementEntity> findByAccount(Id account);
}
