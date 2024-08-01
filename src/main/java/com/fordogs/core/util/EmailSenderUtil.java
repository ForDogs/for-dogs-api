package com.fordogs.core.util;

import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.user.domain.vo.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailSenderUtil {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(Email email, String subject, String templateName, Map<String, Object> variables) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email.formattedEmail());
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(setContext(templateName, variables), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw GlobalErrorCode.internalServerException("이메일 전송 중 오류가 발생하였습니다.");
        }
    }

    private String setContext(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);

        return templateEngine.process(templateName, context);
    }
}
