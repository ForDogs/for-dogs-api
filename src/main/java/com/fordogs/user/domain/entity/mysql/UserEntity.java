package com.fordogs.user.domain.entity.mysql;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.util.crypto.PasswordHasherUtil;
import com.fordogs.user.domain.enums.Provider;
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
@Entity(name = "`user`")
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

    private LocalDate birthDate = LocalDate.of(1990, 1, 1);

    @Embedded
    private Email email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "password"))
    })
    private EncryptedPassword password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.BUYER;

    @Enumerated(EnumType.STRING)
    private Provider provider = Provider.LOCAL;

    private boolean enabled = true;

    @Builder
    public UserEntity(Account account, Name name, Email email, Password password, LocalDate birthDate, Role role, Provider provider) {
        this.account = account;
        this.name = name;
        this.birthDate = (birthDate != null) ? birthDate : LocalDate.of(1990, 1, 1);
        this.email = email;
        this.password = EncryptedPassword.builder()
                .value(PasswordHasherUtil.encode(password.getValue()))
                .build();
        this.role = (role != null) ? role : Role.BUYER;
        this.provider = (provider != null) ? provider : Provider.LOCAL;
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
