package com.example.meetingmanagement.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Date;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class JwtUtil {
    private static final String SECRET = "my-secret-key";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60*24;
    private final ConcurrentHashMap<String, Boolean> invalidTokens = new ConcurrentHashMap<>();

    public String generateToken(Long userId) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public Long validateToken(String token) {
        try {
            if (invalidTokens.containsKey(token)) {
                log.warn("🔴 Invalid Token: {}", token);
                return null;
            }

            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET))
                    .build()
                    .verify(token);

            String subject = decodedJWT.getSubject();
            if (subject == null || subject.isEmpty()) {
                log.warn("⚠️ JWT subject is missing or empty");
                return null;
            }

            try {
                Long userId = Long.valueOf(subject);
                log.info("✅ Extracted User ID: {}", userId);
                return userId;
            } catch (NumberFormatException e) {
                log.error("❌ Invalid subject format in JWT: {}", subject);
                return null;
            }

        } catch (JWTVerificationException e) {
            log.error("❌ JWT Verification Failed: {}", e.getMessage());
            return null;
        }
    }

    public void invalidateToken(String token) {
        invalidTokens.put(token, true);
    }
}