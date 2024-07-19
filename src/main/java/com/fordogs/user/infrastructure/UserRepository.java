package com.fordogs.user.infrastructure;

import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.domain.vo.Email;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.Name;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByAccount(Account account);

    boolean existsByEmail(Email email);

    Optional<UserEntity> findByAccount(Account account);

    Optional<UserEntity> findByNameAndBirthDateAndEmail(Name name, LocalDate birthDate, Email email);
}
