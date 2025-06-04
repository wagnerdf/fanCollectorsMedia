package com.wagnerdf.fancollectorsmedia.security;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private static final String SECRET_KEY = "12345678901234567890123456789012"; // 32 chars (256 bits)
	
	//private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 2; // 2 hora
	
	private static final long EXPIRATION_TIME = 1000 * 20; // 10 segundos para teste

	public String generateToken(Usuario usuario) {
		return Jwts.builder()
			    .setSubject(usuario.getLogin()) // ou .getUsername(), se for o caso
			    .setIssuedAt(new Date(System.currentTimeMillis()))
			    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
			    .signWith(getSignKey(), SignatureAlgorithm.HS256)
			    .compact();
	}

	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	public boolean isTokenValid(String token, String username) {
		return extractUsername(token).equals(username) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return getClaims(token).getExpiration().before(new Date());
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}
	
	public boolean isTokenValido(String token) {
	    try {
	    	Jwts.parserBuilder()
	        .setSigningKey(SECRET_KEY.getBytes())
	        .build()
	        .parseClaimsJws(token);
	        return true;
	    } catch (JwtException | IllegalArgumentException e) {
	        return false;
	    }
	}
	
	public String generateToken(UserDetails userDetails) {
	    Usuario usuario = new Usuario();
	    usuario.setLogin(userDetails.getUsername()); // Assumindo que só precisa do login
	    return generateToken(usuario);
	}
}
