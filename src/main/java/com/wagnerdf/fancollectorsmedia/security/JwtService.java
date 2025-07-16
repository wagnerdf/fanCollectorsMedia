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

    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 2; // 2 horas
    //private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60; // 1 minuto
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60L * 60L * 24L * 7L; // 7 dias

    public String generateToken(Usuario usuario) {
    	
    	Date expirationDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);
    	System.out.println("[JWT] Access Token gerado para '" + usuario.getLogin() + "' com expiração em: " + expirationDate);
    	
        return Jwts.builder()
                .setSubject(usuario.getLogin())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        Usuario usuario = new Usuario();
        usuario.setLogin(userDetails.getUsername());
        return generateToken(usuario);
    }

    public String generateTokenFromUsername(String username) {
        Usuario usuario = new Usuario();
        usuario.setLogin(username);
        return generateToken(usuario);
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    public boolean isTokenValido(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            final String username = extractUsername(token);
            return username != null && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String generateTokenFromRefreshToken(String refreshToken) {
        String username = extractUsername(refreshToken);
        return generateTokenFromUsername(username);
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
