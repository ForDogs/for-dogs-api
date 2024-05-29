package com.fordogs.core.domian.entity;

import com.fordogs.core.domian.enums.Role;
import com.fordogs.core.domian.vo.wapper.EncryptedPassword;
import com.fordogs.core.domian.vo.wapper.Id;
import com.fordogs.core.domian.vo.*;
import com.fordogs.core.domian.vo.wapper.Name;
import com.fordogs.core.domian.vo.wapper.Password;
import com.fordogs.core.util.PasswordUtil;
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
    private Id account;

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
    public UserManagementEntity(Id account, Name name, Email email, Password password, Role role) {
        this.account = account;
        this.name = name;
        this.email = email;
        this.password = EncryptedPassword.builder()
                .value(PasswordUtil.encode(password.getValue()))
                .build();
        this.role = role != null ? role : Role.BUYER;
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }
}
