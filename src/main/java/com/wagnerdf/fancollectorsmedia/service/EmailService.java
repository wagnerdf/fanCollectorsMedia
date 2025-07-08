package com.wagnerdf.fancollectorsmedia.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void enviarEmail(String para, String assunto, String conteudo) {
        System.out.println("---- Email Simulado ----");
        System.out.println("Para: " + para);
        System.out.println("Assunto: " + assunto);
        System.out.println("Conteúdo:\n" + conteudo);
        System.out.println("------------------------");
    }
}

