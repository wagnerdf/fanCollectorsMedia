package com.wagnerdf.fancollectorsmedia.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private static final String SECRET_KEY = "12345678901234567890123456789012"; // 32 chars (256 bits)

	public String generateToken(Usuario usuario) {
		return Jwts.builder()
			    .setSubject(usuario.getEmail()) // ou .getUsername(), se for o caso
			    .setIssuedAt(new Date(System.currentTimeMillis()))
			    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // 2 horas
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
}
