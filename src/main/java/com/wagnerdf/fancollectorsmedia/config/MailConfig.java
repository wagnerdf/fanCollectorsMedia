package com.wagnerdf.fancollectorsmedia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(System.getenv().getOrDefault("SPRING_MAIL_HOST", "smtp.gmail.com"));
        mailSender.setPort(Integer.parseInt(System.getenv().getOrDefault("SPRING_MAIL_PORT", "587")));
        mailSender.setUsername(System.getenv().getOrDefault("SPRING_MAIL_USERNAME", username));
        mailSender.setPassword(System.getenv().getOrDefault("SPRING_MAIL_PASSWORD", password));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
