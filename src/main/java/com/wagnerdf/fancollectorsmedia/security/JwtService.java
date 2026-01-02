package com.wagnerdf.fancollectorsmedia.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${api.config.secret}")
    private String secretKey; // agora vem do application.properties / ambiente

    // private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 2; // 2 horas
    
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60L * 2L;
    
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60L * 60L * 24L * 7L; // 7 dias

    // ------------------ GERAÇÃO DE TOKENS ------------------

    public String generateToken(Usuario usuario) {
        Date expirationDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);
        System.out.println("[JWT] Access Token gerado para '" + usuario.getLogin() + "' com expiração em: " + expirationDate);

        return Jwts.builder()
                .setSubject(usuario.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
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
        Date expirationDate = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION);
        System.out.println("[JWT] Refresh Token gerado para '" + username + "' com expiração em: " + expirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenFromRefreshToken(String refreshToken) {
        String username = extractUsername(refreshToken);
        return generateTokenFromUsername(username);
    }

    // ------------------ VALIDAÇÃO ------------------

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

    // ------------------ EXTRAÇÃO DE DADOS ------------------

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // ------------------ MÉTODOS PÚBLICOS ------------------

    // Agora público para o AuthController usar
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // ------------------ MÉTODOS PRIVADOS ------------------

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
