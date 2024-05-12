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
    public UserEntity(String userIdentifier, String name, String emailId, String emailDomain, String password, Role role) {
        this.userIdentifier = Id.builder().value(userIdentifier).build();
        this.name = Name.builder().value(name).build();
        this.email = Email.builder().id(emailId).domain(emailDomain).build();
        this.password = EncryptedPassword.builder().value(PasswordUtil.encode(password)).build();
        this.role = role != null ? role : Role.BUYER;
    }

    public void disableAccount() {
        this.isDeleted = true;
    }
}
