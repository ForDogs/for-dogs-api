package com.fordogs.security.provider;

import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.Id;
import com.fordogs.core.infrastructure.UserRepository;
import com.fordogs.core.exception.error.SecurityServiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsServiceProvider implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByAccount(Id.builder().value(username).build())
                .orElseThrow(SecurityServiceErrorCode.TOKEN_USER_NOT_FOUND::toException);
        if (!userEntity.isEnabled()) {
            throw SecurityServiceErrorCode.USER_DISABLED.toException();
        }

        return new CustomUserDetails(userEntity);
    }
}
