package com.fordogs.security.provider;

import com.fordogs.user.application.UserManagementService;
import com.fordogs.user.domain.entity.UserManagementEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsServiceProvider implements UserDetailsService {

    private final UserManagementService userManagementService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserManagementEntity userManagementEntity = userManagementService.findByAccount(username);
        userManagementEntity.checkIfEnabled();

        return new CustomUserDetails(userManagementEntity);
    }
}
