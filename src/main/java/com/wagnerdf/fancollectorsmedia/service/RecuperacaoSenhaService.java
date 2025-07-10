package com.wagnerdf.fancollectorsmedia.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.PasswordResetToken;
import com.wagnerdf.fancollectorsmedia.model.Usuario;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.PasswordResetTokenRepository;
import com.wagnerdf.fancollectorsmedia.repository.UsuarioRepository;

@Service
public class RecuperacaoSenhaService {

    @Autowired
    private CadastroRepository cadastroRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void solicitarRecuperacaoSenha(String email) {
        Optional<Cadastro> cadastroOpt = cadastroRepository.findByEmail(email);

        if (cadastroOpt.isPresent()) {
            // Remove tokens anteriores
            tokenRepository.deleteByEmail(email);

            String token = UUID.randomUUID().toString();
            LocalDateTime expiracao = LocalDateTime.now().plusDays(1);// Periodos para expirar o TOKEN: plusMinutes, plusHours e plusDays

            PasswordResetToken tokenEntity = new PasswordResetToken(email, token, expiracao);
            tokenRepository.save(tokenEntity);

            String link = "http://localhost:5173/resetar-senha?token=" + token;
            emailService.enviarEmailRecuperacao(email, token);
        }

        // Independente de existir ou não, não entrega informação
    }
    
    @Transactional
    public void redefinirSenha(String token, String novaSenha) {
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            throw new RuntimeException("A nova senha não pode estar vazia.");
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        Cadastro cadastro = cadastroRepository.findByEmail(resetToken.getEmail())
            .orElseThrow(() -> new RuntimeException("Cadastro não encontrado"));

        Usuario usuario = usuarioRepository.findByCadastro(cadastro)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String novaSenhaHash = passwordEncoder.encode(novaSenha);
        System.out.println("Antes de salvar nova senha: " + usuario.getSenha());
        usuario.setSenha(novaSenhaHash);
        usuarioRepository.save(usuario);
        System.out.println("Depois de salvar nova senha: " + novaSenhaHash);
        tokenRepository.delete(resetToken);
    }


}

