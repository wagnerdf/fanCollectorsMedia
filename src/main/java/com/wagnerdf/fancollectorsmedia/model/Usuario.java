package com.wagnerdf.fancollectorsmedia.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuario")
public class Usuario implements UserDetails {
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cadastro_id")
    private UsuarioCadastro usuarioCadastro;

    @Column(unique = true)
    @NotBlank(message = "Login não pode ser vazio")
    private String login; // será o email do usuário
    
    @NotBlank(message = "Login não pode ser vazio")
    private String senha;

    private String papel; // Exemplo: "ROLE_USER", "ROLE_ADMIN"

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(papel));
    }

	@Override
	public String getPassword() {
	    return senha;  // sua senha
	}

	@Override
	public String getUsername() {
	    return login;  // seu login, que é o email
	}

	@Override
	public boolean isAccountNonExpired() {
	    return true; // ou lógica de expiração se houver
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true; // ou lógica de bloqueio se houver
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true; // ou lógica de expiração da credencial
	}

	@Override
	public boolean isEnabled() {
	    return true; // ou lógica para verificar se o usuário está ativo
	}
}