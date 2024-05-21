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

    public static RefreshTokenEntity create(UserEntity user, RefreshToken refreshToken) {
        if (user == null || refreshToken == null) {
            throw new IllegalArgumentException("필수 인자가 부족하여 RefreshTokenEntity를 생성할 수 없습니다.");
        }

        return RefreshTokenEntity.builder()
                .user(user)
                .token(refreshToken)
                .build();
    }
}
