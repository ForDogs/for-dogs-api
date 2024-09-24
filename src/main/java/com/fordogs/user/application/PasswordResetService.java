package com.fordogs.user.application;

import com.fordogs.core.exception.DomainException;
import com.fordogs.core.util.EmailSenderUtil;
import com.fordogs.core.util.StringGenerator;
import com.fordogs.core.util.constants.EmailConstants;
import com.fordogs.core.util.crypto.PasswordHasherUtil;
import com.fordogs.user.domain.entity.UserEntity;
import com.fordogs.user.domain.cache.EmailAuthCache;
import com.fordogs.user.domain.vo.Email;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.Password;
import com.fordogs.user.error.PasswordResetErrorCode;
import com.fordogs.user.infrastructure.EmailAuthRepository;
import com.fordogs.user.presentation.request.UserPasswordChangeRequest;
import com.fordogs.user.presentation.request.UserPasswordResetRequest;
import com.fordogs.user.presentation.request.UserPasswordResetVerifyRequest;
import com.fordogs.user.presentation.response.UserPasswordResetVerifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PasswordResetService {

    @Value("${spring.mail.auth-code-expiration-minutes}")
    private long authCodeExpirationMinutes;

    private final EmailAuthRepository emailAuthRepository;
    private final EmailSenderUtil emailSenderUtil;
    private final UserQueryService userQueryService;

    public void requestPasswordReset(UserPasswordResetRequest request) {
        Email requestEmail = request.toEmail();
        UserEntity userEntity = userQueryService.findByAccount(Account.builder().value(request.getUserId()).build());

        if (!request.getUserId().equals(userEntity.getAccount().getValue())
                || !requestEmail.equals(userEntity.getEmail())) {
            throw PasswordResetErrorCode.USER_NOT_FOUND.toException();
        }

        String authenticationCode = StringGenerator.generate4DigitString();
        emailSenderUtil.sendMail(requestEmail, EmailConstants.PASSWORD_RESET_EMAIL_SUBJECT, "email", Map.of("code", authenticationCode));

        EmailAuthCache emailAuthCache = EmailAuthCache.builder()
                .authCode(authenticationCode)
                .userAccount(request.getUserId())
                .expirationTime(authCodeExpirationMinutes)
                .build();

        emailAuthRepository.save(emailAuthCache);
    }

    public UserPasswordResetVerifyResponse verifyPasswordReset(UserPasswordResetVerifyRequest request) {
        EmailAuthCache emailAuthCache = emailAuthRepository.findById(request.getAuthCode())
                .orElseThrow(PasswordResetErrorCode.AUTH_CODE_NOT_FOUND::toException);

        String newPassword = generateAndSetPassword(emailAuthCache);

        return UserPasswordResetVerifyResponse.builder()
                .temporaryPassword(newPassword)
                .build();
    }

    private String generateAndSetPassword(EmailAuthCache emailAuthCache) {
        String newPassword = StringGenerator.generatePassword();

        try {
            UserEntity userEntity = userQueryService.findByAccount(Account.builder()
                    .value(emailAuthCache.getUserAccount())
                    .build());

            userEntity.changePassword(Password.builder()
                    .value(newPassword)
                    .build());

            emailAuthRepository.delete(emailAuthCache);

            return newPassword;
        } catch (DomainException e) {
            return generateAndSetPassword(emailAuthCache);
        }
    }

    public void changePassword(UserPasswordChangeRequest request) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity userEntity = userQueryService.findById(userId);

        if (!PasswordHasherUtil.matches(request.getCurrentPassword(), userEntity.getPassword().getValue())) {
            throw PasswordResetErrorCode.EXISTING_PASSWORD_MISMATCH.toException();
        }
        userEntity.changePassword(Password.builder().value(request.getNewPassword()).build());
    }
}
