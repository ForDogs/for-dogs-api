package com.fordogs.user.infrastructure;

import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import com.fordogs.user.domain.vo.wrapper.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserManagementRepository extends JpaRepository<UserManagementEntity, UUID> {

    boolean existsByAccount(Account account);

    Optional<UserManagementEntity> findByAccount(Account account);
}
