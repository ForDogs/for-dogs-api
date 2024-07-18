package com.fordogs.user.application.email;

import com.fordogs.core.exception.error.GlobalErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailSender {

    private static final String EMAIL_SUBJECT = "[For Dogs] 비밀번호 찾기 인증코드 안내";

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(String userEmail, String authenticationCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject(EMAIL_SUBJECT);
            mimeMessageHelper.setText(setContext(authenticationCode), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw GlobalErrorCode.internalServerException("인증 이메일 전송 중 오류가 발생하였습니다.");
        }
    }

    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);

        return templateEngine.process("email", context);
    }
}
