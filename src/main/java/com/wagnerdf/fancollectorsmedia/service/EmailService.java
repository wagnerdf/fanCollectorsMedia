package com.wagnerdf.fancollectorsmedia.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
public class EmailService {

    // Vamos garantir que ele leia direto do ambiente, com fallback para application.properties
	@Value("${sendgrid.api.key:}")
	private String sendGridApiKeyFromSpring;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void enviarEmailRecuperacao(String destinatario, String token) {
        // 1️⃣ Pegar a API Key — com prioridade para variável de ambiente
    	String apiKey = System.getenv("SENDGRID_API_KEY");
    	if (apiKey == null || apiKey.isBlank()) {
    	    apiKey = sendGridApiKeyFromSpring;
    	}

        String assunto = "Recuperação de Senha - FanCollectionMidia";
        String link = frontendUrl + "/resetar-senha?token=" + token;
        String conteudo = "Olá!\n\nClique no link abaixo para redefinir sua senha:\n\n" + link +
                "\n\nSe você não solicitou isso, ignore este e-mail.";

        // ✅ Remetente verificado no SendGrid
        Email from = new Email("wagner.lorddf@gmail.com", "FanCollectionMidia");
        Email to = new Email(destinatario);
        Content content = new Content("text/plain", conteudo);
        Mail mail = new Mail(from, assunto, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("SendGrid Status: " + response.getStatusCode());
            //System.out.println("SendGrid Body: " + response.getBody());
            //System.out.println("SendGrid Headers: " + response.getHeaders());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao enviar e-mail de recuperação: " + e.getMessage());
        }
    }
}
