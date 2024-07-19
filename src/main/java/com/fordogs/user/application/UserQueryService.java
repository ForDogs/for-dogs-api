package com.fordogs.user.application;

import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.error.UserErrorCode;
import com.fordogs.user.infrastructure.UserRepository;
import com.fordogs.user.presentation.request.UserFindIdRequest;
import com.fordogs.user.presentation.response.UserDetailsResponse;
import com.fordogs.user.presentation.response.UserFindIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    public UserDetailsResponse findUserDetails() {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity userEntity = findById(userId);

        return UserDetailsResponse.toResponse(userEntity);
    }

    public UserFindIdResponse findUserAccountByNameAndBirthDate(UserFindIdRequest request) {
        UserEntity userEntity = userRepository.findByNameAndBirthDateAndEmail(
                request.toName(),
                request.getUserBirthDate(),
                request.toEmail()
        ).orElseThrow(UserErrorCode.USER_NOT_FOUND::toException);

        return UserFindIdResponse.toResponse(userEntity);
    }

    public UserEntity findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserErrorCode.USER_NOT_FOUND::toException);
    }

    public UserEntity findByAccount(Account account) {
        return userRepository.findByAccount(account)
                .orElseThrow(UserErrorCode.USER_NOT_FOUND::toException);
    }
}
