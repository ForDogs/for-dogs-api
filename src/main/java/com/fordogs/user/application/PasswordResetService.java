package com.fordogs.user.application;

import com.fordogs.core.util.StringGenerator;
import com.fordogs.user.application.email.EmailSender;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.domain.entity.redis.EmailAuthCache;
import com.fordogs.user.domain.vo.wrapper.Account;
import com.fordogs.user.domain.vo.wrapper.Password;
import com.fordogs.user.error.PasswordResetErrorCode;
import com.fordogs.user.infrastructure.EmailAuthRepository;
import com.fordogs.user.presentation.request.UserPasswordResetRequest;
import com.fordogs.user.presentation.request.UserPasswordResetVerifyRequest;
import com.fordogs.user.presentation.response.UserPasswordResetVerifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PasswordResetService {

    @Value("${spring.mail.auth-code-expiration-minutes}")
    private long authCodeExpirationMinutes;

    private final EmailAuthRepository emailAuthRepository;
    private final EmailSender emailSender;
    private final UserQueryService userQueryService;

    public void requestPasswordReset(UserPasswordResetRequest request) {
        UserEntity userEntity = userQueryService.findByAccount(Account.builder().value(request.getUserId()).build());

        if (!request.getUserId().equals(userEntity.getAccount().getValue())
                || !request.getUserEmail().equals(userEntity.getEmail().formattedEmail())) {
            throw PasswordResetErrorCode.USER_NOT_FOUND.toException();
        }

        String authenticationCode = StringGenerator.generate4DigitString();
        emailSender.sendMail(request.getUserEmail(), authenticationCode);

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

        String newPassword = StringGenerator.generatePassword();

        UserEntity userEntity = userQueryService.findByAccount(Account.builder().value(emailAuthCache.getUserAccount()).build());
        userEntity.changePassword(Password.builder()
                .value(newPassword)
                .build());

        emailAuthRepository.delete(emailAuthCache);

        return UserPasswordResetVerifyResponse.builder()
                .temporaryPassword(newPassword)
                .build();
    }
}
