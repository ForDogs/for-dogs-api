package com.fordogs.security.provider;

import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.infrastructure.UserManagementRepository;
import com.fordogs.core.exception.error.SecurityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsServiceProvider implements UserDetailsService {

    private final UserManagementRepository userManagementRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserManagementEntity userManagementEntity = userManagementRepository.findByAccount(Id.builder().value(username).build())
                .orElseThrow(SecurityErrorCode.USER_DISABLED::toException);
        if (!userManagementEntity.isEnabled()) {
            throw SecurityErrorCode.USER_DISABLED.toException();
        }

        return new CustomUserDetails(userManagementEntity);
    }
}
