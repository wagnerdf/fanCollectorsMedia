package com.wagnerdf.fancollectorsmedia.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario implements UserDetails {
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	// Relacionamento com UsuarioCadastro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cadastro_id")
    private UsuarioCadastro usuarioCadastro;

    @NotBlank(message = "Login não pode ser vazio")
    private String login; // será o email do usuário
    
    @NotBlank(message = "Login não pode ser vazio")
    private String senha;

    private String papel; // Exemplo: "ROLE_USER", "ROLE_ADMIN"

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(papel));
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsuarioCadastro getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(UsuarioCadastro usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getPapel() {
		return papel;
	}

	public void setPapel(String papel) {
		this.papel = papel;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
    
    ////
	@Override
	public String getPassword() {
	    return senha;  // sua senha
	}

	@Override
	public String getUsername() {
	    return login;  // seu login, que é o username
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
