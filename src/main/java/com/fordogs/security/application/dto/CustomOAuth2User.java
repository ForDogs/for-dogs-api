package com.fordogs.security.application.dto;

import com.fordogs.user.domain.entity.UserEntity;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Builder
public record CustomOAuth2User(
        UserEntity userEntity,
        Map<String, Object> attributes,
        String attributeKey) implements OAuth2User {

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().name()));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get(attributeKey));
    }
}
