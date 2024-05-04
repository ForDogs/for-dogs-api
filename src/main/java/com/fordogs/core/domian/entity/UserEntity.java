package com.fordogs.core.domian.entity;

import com.fordogs.core.domian.enums.Role;
import com.fordogs.core.domian.vo.user.*;
import com.fordogs.core.domian.vo.user.Id;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class UserEntity extends BaseEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "userId"))
    })
    private Id userId;

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
    public UserEntity(Id userId, Name name, Email email, Password password, Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = EncryptedPassword.encodePassword(password);
        this.role = role;
    }

    public static Role getRoleOrDefault(Role role) {
        return role != null ? role : Role.BUYER;
    }
}
