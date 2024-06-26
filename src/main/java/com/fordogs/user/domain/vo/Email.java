package com.fordogs.user.domain.vo;

import com.fordogs.core.domain.vo.ValueObject;
import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.user.error.UserManagementErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends ValueObject {

    @Column(name = "email_id")
    private String id;

    @Column(name = "email_domain")
    private String domain;

    @Builder
    private Email(String id, String domain) {
        validate(id, domain);
        this.id = id;
        this.domain = domain;
    }

    public String formattedEmail() {
        return getId() + "@" + getDomain();
    }

    private void validate(String id, String domain) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("이메일 ID가 존재하지 않습니다.");
        }
        if (domain == null || domain.isEmpty()) {
            throw new IllegalArgumentException("이메일 도메인이 존재하지 않습니다.");
        }
        if (!StringValidator.validateEnglishNumber(id)) {
            throw UserManagementErrorCode.INVALID_EMAIL_ID.toException();
        }
        if (!StringValidator.validateEmailDomainPattern(domain)) {
            throw UserManagementErrorCode.INVALID_EMAIL_DOMAIN.toException();
        }
    }
}
