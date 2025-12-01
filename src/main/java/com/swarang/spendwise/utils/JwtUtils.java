package com.swarang.spendwise.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${jwt.expiration}")
    private Long expirationMs;

    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder b64Encoder = Base64.getUrlEncoder().withoutPadding();

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractClaim(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserNameFromToken(String token) {
        return extractClaim(token).getSubject();
    }

    public String generateRefreshToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        return b64Encoder.encodeToString(bytes);
    }

    public String generateTokenHash(String token) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteHash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return b64Encoder.encodeToString(byteHash);
        }
        catch (NoSuchAlgorithmException ex){
            throw new IllegalStateException("Failed to hash refresh token, SHA-256 algorithm not available");
        }
    }
}
