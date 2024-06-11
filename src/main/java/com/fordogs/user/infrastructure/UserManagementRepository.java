package com.fordogs.user.infrastructure;

import com.fordogs.user.domain.entity.UserManagementEntity;
import com.fordogs.user.domain.vo.wrapper.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserManagementRepository extends JpaRepository<UserManagementEntity, UUID> {

    boolean existsByAccount(Id account);

    Optional<UserManagementEntity> findByAccount(Id account);
}
