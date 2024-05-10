package com.fordogs.core.domian.entity;

import com.fordogs.core.domian.vo.RefreshToken;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "refresh_token")
public class RefreshTokenEntity extends BaseEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "token"))
    })
    private RefreshToken token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    public static RefreshTokenEntity createRefreshTokenEntity(UserEntity user, RefreshToken token) {
        return RefreshTokenEntity.builder()
                .user(user)
                .token(token)
                .build();
    }
}
