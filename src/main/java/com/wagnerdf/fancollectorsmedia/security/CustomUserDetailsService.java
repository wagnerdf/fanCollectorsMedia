package com.wagnerdf.fancollectorsmedia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o login: " + login));
    }
}

