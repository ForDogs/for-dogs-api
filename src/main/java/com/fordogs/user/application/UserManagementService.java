package com.fordogs.user.application;

import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.constants.HttpRequestConstants;
import com.fordogs.user.domain.entity.UserManagementEntity;
import com.fordogs.user.domain.vo.wrapper.Id;
import com.fordogs.user.error.UserManagementErrorCode;
import com.fordogs.user.infrastructure.UserManagementRepository;
import com.fordogs.user.presentation.request.UserSignupRequest;
import com.fordogs.user.presentation.response.UserDetailsResponse;
import com.fordogs.user.presentation.response.UserSignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserManagementService {

    private final UserManagementRepository userManagementRepository;

    @Transactional
    public UserSignupResponse signupUser(UserSignupRequest request) {
        UserManagementEntity userManagementEntity = request.toEntity();

        if (userManagementRepository.existsByAccount(userManagementEntity.getAccount())) {
            throw UserManagementErrorCode.DUPLICATE_USER_ID.toException();
        }

        UserManagementEntity savedUserManagementEntity = userManagementRepository.save(userManagementEntity);

        return UserSignupResponse.toResponse(savedUserManagementEntity);
    }

    @Transactional
    public void deactivateUser() {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(HttpRequestConstants.REQUEST_ATTRIBUTE_USER_ID);
        UserManagementEntity userManagementEntity = findById(userId);
        userManagementEntity.disable();
    }

    public UserDetailsResponse findUserDetails() {
        UUID userId = (UUID) HttpServletUtil.getRequestAttribute(HttpRequestConstants.REQUEST_ATTRIBUTE_USER_ID);
        UserManagementEntity userManagementEntity = findById(userId);

        return UserDetailsResponse.toResponse(userManagementEntity);
    }

    public UserManagementEntity findById(UUID userId) {
        return userManagementRepository.findById(userId)
                .orElseThrow(UserManagementErrorCode.USER_NOT_FOUND::toException);
    }

    public UserManagementEntity findByAccount(String account) {
        return userManagementRepository.findByAccount(Id.builder().value(account).build())
                .orElseThrow(UserManagementErrorCode.USER_NOT_FOUND::toException);
    }
}
