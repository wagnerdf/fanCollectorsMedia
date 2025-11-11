package com.wagnerdf.fancollectorsmedia.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${spring.mail.password}")
    private String sendGridApiKey;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void enviarEmailRecuperacao(String destinatario, String token) {
        String assunto = "Recuperação de Senha - fanCollectorsMedia";
        String link = frontendUrl + "/resetar-senha?token=" + token;
        String conteudo = "Olá!\n\nClique no link abaixo para redefinir sua senha:\n\n" + link +
                "\n\nSe você não solicitou isso, ignore este e-mail.";

        Email from = new Email("suporte@fancollectorsmidia.com", "FanCollectorsMidia");
        Email to = new Email(destinatario);
        Content content = new Content("text/plain", conteudo);
        Mail mail = new Mail(from, assunto, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            // Opcional: Log de resposta para debug
            System.out.println("SendGrid Status: " + response.getStatusCode());
            System.out.println("SendGrid Body: " + response.getBody());
            System.out.println("SendGrid Headers: " + response.getHeaders());

        } catch (IOException e) {
            throw new RuntimeException("Erro ao enviar e-mail de recuperação: " + e.getMessage());
        }
    }
}
