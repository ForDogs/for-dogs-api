package com.fordogs.security.model;

import com.fordogs.user.domain.enums.Role;
import com.fordogs.user.domain.vo.wrapper.Account;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomAuthentication implements Authentication {

    private Account account;
    private String id;
    private Role role;
    private boolean authenticated;

    @Builder
    public CustomAuthentication(String account, String id, String role) {
        this.account = Account.builder().value(account).build();
        this.id = id;
        this.role = Role.valueOf(role);
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return account;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return id;
    }
}
