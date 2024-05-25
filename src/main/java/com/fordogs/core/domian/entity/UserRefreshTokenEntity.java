package com.fordogs.core.domian.entity;

import com.fordogs.core.domian.vo.RefreshToken;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "refresh_token")
public class UserRefreshTokenEntity extends BaseEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "token"))
    })
    private RefreshToken token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserManagementEntity user;

    public static UserRefreshTokenEntity create(UserManagementEntity user, RefreshToken refreshToken) {
        if (user == null || refreshToken == null) {
            throw new IllegalArgumentException("회원 정보와 토큰이 유효하지 않아 RefreshTokenEntity 생성이 불가합니다.");
        }

        return UserRefreshTokenEntity.builder()
                .user(user)
                .token(refreshToken)
                .build();
    }
}
