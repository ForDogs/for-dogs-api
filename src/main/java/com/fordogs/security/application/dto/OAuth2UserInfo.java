package com.fordogs.security.application.dto;

import com.fordogs.core.util.StringGenerator;
import com.fordogs.security.exception.error.SecurityErrorCode;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.domain.enums.Provider;
import com.fordogs.user.domain.enums.Role;
import com.fordogs.user.domain.vo.Email;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.Name;
import com.fordogs.user.domain.vo.wrapper.Password;
import lombok.Builder;

import java.util.Map;

@Builder
public record OAuth2UserInfo(
        Account account,
        Name name,
        Email email,
        Provider provider) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw SecurityErrorCode.ILLEGAL_REGISTRATION_ID.toException();
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return buildOAuth2UserInfo(
                (String) attributes.get("sub"),
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                Provider.GOOGLE
        );
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return buildOAuth2UserInfo(
                String.valueOf(attributes.get("id")),
                (String) profile.get("nickname"),
                (String) account.get("email"),
                Provider.KAKAO
        );
    }

    private static OAuth2UserInfo buildOAuth2UserInfo(String accountValue, String nameValue, String emailValue, Provider provider) {
        return OAuth2UserInfo.builder()
                .account(Account.builder().value(accountValue).build())
                .name(Name.builder().value(nameValue).build())
                .email(Email.fromFormattedEmail(emailValue))
                .provider(provider)
                .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .account(account)
                .name(name)
                .email(email)
                .password(Password.builder()
                        .value(StringGenerator.generatePassword())
                        .build())
                .role(Role.BUYER)
                .provider(provider)
                .build();
    }
}
