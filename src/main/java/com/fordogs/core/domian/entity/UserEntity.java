package com.fordogs.core.domian.entity;

import com.fordogs.core.domian.enums.Role;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.domian.vo.*;
import com.fordogs.core.util.PasswordUtil;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class UserEntity extends BaseEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "userIdentifier"))
    })
    private Id userIdentifier;

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

    private boolean isDeleted = false;

    @Builder
    public UserEntity(Id userIdentifier, Name name, Email email, Password password, Role role) {
        this.userIdentifier = userIdentifier;
        this.name = name;
        this.email = email;
        this.password = EncryptedPassword.builder()
                .value(PasswordUtil.encode(password.getValue()))
                .build();
        this.role = role != null ? role : Role.BUYER;
    }

    public void disableAccount() {
        this.isDeleted = true;
    }
}
