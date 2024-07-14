package com.fordogs.security.authentication;

import com.fordogs.core.exception.error.GlobalErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtAuthentication implements Authentication {
    private String account;
    private String id;
    private String role;
    private boolean isAuthenticated;

    @Builder
    public JwtAuthentication(String account, String id, String role) {
        validate(account, id, role);
        this.account = account;
        this.id = id;
        this.role = role;
        this.isAuthenticated = true;
    }

    private void validate(String account, String id, String role) {
        if (account == null || account.isEmpty()) {
            throw GlobalErrorCode.internalServerException("회원 계정 ID가 존재하지 않습니다.");
        }
        if (id == null || id.isEmpty()) {
            throw GlobalErrorCode.internalServerException("회원 식별자가 존재하지 않습니다.");
        }
        if (role == null || role.isEmpty()) {
            throw GlobalErrorCode.internalServerException("회원 권한이 존재하지 않습니다.");
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public Object getCredentials() {
        return this.account;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        if (id != null && isAuthenticated) {
            return id;
        }
        return null;
    }
}
