package com.wagnerdf.fancollectorsmedia.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.PasswordResetToken;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.PasswordResetTokenRepository;

@Service
public class RecuperacaoSenhaService {

    @Autowired
    private CadastroRepository cadastroRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    public void solicitarRecuperacaoSenha(String email) {
        Optional<Cadastro> cadastroOpt = cadastroRepository.findByEmail(email);

        if (cadastroOpt.isPresent()) {
            // Remove tokens anteriores
            tokenRepository.deleteByEmail(email);

            String token = UUID.randomUUID().toString();
            LocalDateTime expiracao = LocalDateTime.now().plusMinutes(30);

            PasswordResetToken tokenEntity = new PasswordResetToken(email, token, expiracao);
            tokenRepository.save(tokenEntity);

            String link = "http://localhost:5173/resetar-senha?token=" + token;
            emailService.enviarEmail(email, "Recuperação de senha", "Clique no link para redefinir sua senha:\n\n" + link);
        }

        // Independente de existir ou não, não entrega informação
    }
}

