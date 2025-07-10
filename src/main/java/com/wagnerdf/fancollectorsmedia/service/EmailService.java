package com.wagnerdf.fancollectorsmedia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailRecuperacao(String destinatario, String token) {
        String assunto = "Recuperação de Senha - fanCollectorsMedia";
        String link = "http://localhost:3000/resetar-senha?token=" + token;
        String conteudo = "Olá!\n\nClique no link abaixo para redefinir sua senha:\n\n" + link +
                "\n\nSe você não solicitou isso, ignore este e-mail.";

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject(assunto);
        mensagem.setText(conteudo);

        mailSender.send(mensagem);
    }
}

