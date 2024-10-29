package com.fordogs.chat.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_rooms")
public class ChatRoomEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    private boolean deleted = false;
}
