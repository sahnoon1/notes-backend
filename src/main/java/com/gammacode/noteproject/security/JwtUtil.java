package com.gammacode.noteproject.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    @org.springframework.beans.factory.annotation.Value("${jwt.secret}")
    private String jwtSecret;
    private final long accessTokenValidity = 15 * 60 * 1000; // 15 minutes
    private final long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000; // 7 days

    public String generateAccessToken(UUID userId) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + accessTokenValidity;
        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(new Date(nowMillis))
            .expiration(new Date(expMillis))
            .signWith(key)
            .compact();
    }

    public String generateRefreshToken(UUID userId) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + refreshTokenValidity;
        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(new Date(nowMillis))
            .expiration(new Date(expMillis))
            .signWith(key)
            .compact();
    }

    public UUID validateTokenAndGetUserId(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            return UUID.fromString(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
