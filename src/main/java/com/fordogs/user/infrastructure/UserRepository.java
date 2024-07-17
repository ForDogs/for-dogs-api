package com.fordogs.user.infrastructure;

import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.domain.vo.wrapper.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByAccount(Account account);

    Optional<UserEntity> findByAccount(Account account);
}
