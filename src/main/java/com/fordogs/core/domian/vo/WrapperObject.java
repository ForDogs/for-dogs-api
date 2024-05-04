package com.fordogs.core.domian.vo;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class WrapperObject<T> {

    private T value;

    protected WrapperObject(T value) {
        if (value == null) {
            throw new IllegalArgumentException(this.getClass().getName() + "가 존재하지 않습니다.");
        }
        this.value = value;
    }

    protected abstract void validate(String value);
}
