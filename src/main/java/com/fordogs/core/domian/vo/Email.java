package com.fordogs.core.domian.vo;

import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.core.exception.error.UserManagementServiceErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

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
        if (id == null) {
            throw new IllegalArgumentException("이메일 ID가 존재하지 않습니다.");
        }
        if (domain == null) {
            throw new IllegalArgumentException("이메일 도메인이 존재하지 않습니다.");
        }
        if (!StringValidator.validateEnglishNumber(id)) {
            throw UserManagementServiceErrorCode.INVALID_EMAIL_ID.toException();
        }
        if (!StringValidator.validateEmailDomainPattern(domain)) {
            throw UserManagementServiceErrorCode.INVALID_EMAIL_DOMAIN.toException();
        }
    }
}
