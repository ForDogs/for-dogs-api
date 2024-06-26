package com.fordogs.user.domain.entity.mysql;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.util.crypto.PasswordHasherUtil;
import com.fordogs.user.domain.enums.Role;
import com.fordogs.user.domain.vo.Email;
import com.fordogs.user.domain.vo.wrapper.EncryptedPassword;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.Name;
import com.fordogs.user.domain.vo.wrapper.Password;
import com.fordogs.user.error.UserManagementErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class UserManagementEntity extends BaseEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "account"))
    })
    private Account account;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "name"))
    })
    private Name name;

    @Embedded
    private Email email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "password"))
    })
    private EncryptedPassword password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.BUYER;

    private boolean enabled = true;

    @Builder
    public UserManagementEntity(Account account, Name name, Email email, Password password, Role role) {
        this.account = account;
        this.name = name;
        this.email = email;
        this.password = EncryptedPassword.builder()
                .value(PasswordHasherUtil.encode(password.getValue()))
                .build();
        this.role = role != null ? role : Role.BUYER;
        this.enabled = true;
    }

    public void validateRole(Role role) {
        if (!this.role.equals(role)) {
            throw UserManagementErrorCode.USER_ROLE_MISMATCH.toException();
        }
    }

    public void validatePassword(String requestPassword) {
        if (!PasswordHasherUtil.matches(requestPassword, this.password.getValue())) {
            throw UserManagementErrorCode.LOGIN_PASSWORD_FAILED.toException();
        }
    }

    public void validateIfEnabled() {
        if (!this.enabled) {
            throw UserManagementErrorCode.USER_DISABLED.toException();
        }
    }

    public void disable() {
        this.enabled = false;
    }
}
