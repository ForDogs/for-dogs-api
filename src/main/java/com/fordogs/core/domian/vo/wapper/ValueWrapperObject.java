package com.fordogs.core.domian.vo.wapper;

import com.fordogs.core.domian.vo.ValueObject;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class ValueWrapperObject<T> extends ValueObject {

    private T value;

    protected ValueWrapperObject(T value) {
        if (value == null) {
            throw new IllegalArgumentException(this.getClass().getName() + "가 존재하지 않습니다.");
        }
        this.value = value;
    }

    protected abstract void validate(T value);

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ValueWrapperObject)) {
            return false;
        }
        if (!object.getClass()
                   .getTypeName()
                   .equals(this.getClass()
                               .getTypeName())) {
            return false;
        }
        return value.equals(((ValueWrapperObject<?>) object).getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, this.getClass());
    }
}
