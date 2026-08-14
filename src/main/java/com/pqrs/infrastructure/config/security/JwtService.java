package com.pqrs.infrastructure.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private final byte[] secretKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtService(@Value("${security.jwt.secret}") String secret) {
        this.secretKey = secret.getBytes(StandardCharsets.UTF_8);
    }

    public AuthenticatedUser validateAndExtractUser(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String headerAndPayload = parts[0] + "." + parts[1];
        String expectedSign = sign(headerAndPayload);
        if (!constantTimeEquals(expectedSign, parts[2])) {
            throw new IllegalArgumentException("Invalid token signature");
        }

        try {
            byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
            Map<String, Object> claims = objectMapper.readValue(payloadBytes, Map.class);

            long exp = ((Number) claims.getOrDefault("exp", Long.MAX_VALUE)).longValue();
            if (exp * 1000 < System.currentTimeMillis()) {
                throw new IllegalArgumentException("Expired token");
            }

            String username = (String) claims.get("sub");
            String dependency = (String) claims.get("dependency");
            List<String> roles = (List<String>) claims.getOrDefault("roles", List.of());

            return new AuthenticatedUser(username, dependency, roles);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("The token could not be processed", e);
        }
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Error signing token", e);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
