package com.fordogs.user.domain.entity.mysql;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.util.crypto.PasswordHasherUtil;
import com.fordogs.user.domain.enums.Role;
import com.fordogs.user.domain.vo.Email;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.EncryptedPassword;
import com.fordogs.user.domain.vo.wrapper.Name;
import com.fordogs.user.domain.vo.wrapper.Password;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class UserEntity extends BaseEntity {

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

    private LocalDate birthDate;

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
    public UserEntity(Account account, Name name, Email email, Password password, Role role, LocalDate birthDate) {
        this.account = account;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.password = EncryptedPassword.builder()
                .value(PasswordHasherUtil.encode(password.getValue()))
                .build();
        this.role = role != null ? role : Role.BUYER;
        this.enabled = true;
    }

    public void changePassword(Password newPassword) {
        this.password = EncryptedPassword.builder()
                .value(PasswordHasherUtil.encode(newPassword.getValue()))
                .build();
    }

    public void disable() {
        this.enabled = false;
    }
}
