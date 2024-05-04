package com.fordogs.core.domian.vo.user;

import com.fordogs.core.util.StringValidator;
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
            throw new IllegalArgumentException("이메일 ID는 영문, 숫자만 입력해주세요.");
        }
        if (!StringValidator.validateEmailDomainPattern(domain)) {
            throw new IllegalArgumentException("유효한 이메일 도메인 형식을 입력해주세요.");
        }
    }
}
