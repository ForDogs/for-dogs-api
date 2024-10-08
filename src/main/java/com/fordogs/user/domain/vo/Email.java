package com.fordogs.user.domain.vo;

import com.fordogs.core.domain.vo.ValueObject;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.user.error.UserValidationErrorCode;
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
    public Email(String id, String domain) {
        validate(id, domain);
        this.id = id;
        this.domain = domain;
    }

    public String formattedEmail() {
        return getId() + "@" + getDomain();
    }

    public static Email fromFormattedEmail(String formattedEmail) {
        if (formattedEmail == null || formattedEmail.isEmpty()) {
            throw GlobalErrorCode.internalServerException("이메일 형식이 유효하지 않습니다.");
        }

        String[] parts = formattedEmail.split("@");
        if (parts.length != 2) {
            throw GlobalErrorCode.internalServerException("이메일 형식이 유효하지 않습니다.");
        }

        String id = parts[0];
        String domain = parts[1];

        return Email.builder()
                .id(id)
                .domain(domain)
                .build();
    }

    private void validate(String id, String domain) {
        if (id == null || id.isEmpty()) {
            throw GlobalErrorCode.internalServerException("이메일 ID가 존재하지 않습니다.");
        }
        if (domain == null || domain.isEmpty()) {
            throw GlobalErrorCode.internalServerException("이메일 도메인이 존재하지 않습니다.");
        }
        if (!StringValidator.validateEnglishNumber(id)) {
            throw UserValidationErrorCode.INVALID_EMAIL_ID.toException();
        }
        if (!StringValidator.validateEmailDomainPattern(domain)) {
            throw UserValidationErrorCode.INVALID_EMAIL_DOMAIN.toException();
        }
    }
}
