package com.edu.eci.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;  // Esta clave se puede configurar desde application.properties

    private final long expirationTime = 1000 * 60 * 60; // 1 hora

    // Método para generar un token JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Expiración de 1 hora
                .signWith(SignatureAlgorithm.HS256, secretKey) // Firmar el token con la clave secreta
                .compact();
    }

    // Método para extraer el nombre de usuario del token JWT
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Método para extraer las reclamaciones (claims) del token
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Método para verificar si un token está expirado
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Método para validar el token JWT
    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Método para generar un token JWT con un usuario específico
    public String createToken(String username) {
        return generateToken(username);
    }
}
