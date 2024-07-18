package com.fordogs.configuraion;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class JavaMailSenderConfiguration {

    @Bean
    public MailProperties mailProperties() {
        return new MailProperties();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailProperties().getHost());
        javaMailSender.setPort(mailProperties().getPort());
        javaMailSender.setUsername(mailProperties().getUsername());
        javaMailSender.setPassword(mailProperties().getPassword());
        javaMailSender.setJavaMailProperties(getEmailProperties());

        return javaMailSender;
    }

    private Properties getEmailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        return properties;
    }
}
