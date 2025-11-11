package com.wagnerdf.fancollectorsmedia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${spring.mail.host:smtp.gmail.com}")
    private String host;

    @Value("${spring.mail.port:587}")
    private int port;

    @Value("${app.mail.mode:smtp}") // nova variável — "smtp" ou "sendgrid"
    private String mailMode;

    @Bean
    public JavaMailSender javaMailSender() {
        // Só cria o JavaMailSender se o modo for SMTP
        if (!"smtp".equalsIgnoreCase(mailMode)) {
            System.out.println("📨 Modo SendGrid ativo — JavaMailSender não será inicializado.");
            return null;
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(System.getenv().getOrDefault("SPRING_MAIL_HOST", host));
        mailSender.setPort(Integer.parseInt(System.getenv().getOrDefault("SPRING_MAIL_PORT", String.valueOf(port))));
        mailSender.setUsername(System.getenv().getOrDefault("SPRING_MAIL_USERNAME", username));
        mailSender.setPassword(System.getenv().getOrDefault("SPRING_MAIL_PASSWORD", password));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");

        System.out.println("✅ Modo SMTP ativo — JavaMailSender configurado com host: " + mailSender.getHost());

        return mailSender;
    }
}
