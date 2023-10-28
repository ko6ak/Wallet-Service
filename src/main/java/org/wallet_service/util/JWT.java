package org.wallet_service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Класс обслуживающий токен для аутентификации сессии Игрока.
 */
public final class JWT {
    private static final SecretKey secretKey = Keys.hmacShaKeyFor("UGFzc3dvcmRFbmNvZGVyX3Bhc3N3b3JkRW5jb2Rlcg".getBytes(StandardCharsets.UTF_8));

    private JWT() {
    }

    public static String create(Player player) {
        return Jwts.builder()
                .setSubject(player.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(secretKey)
                .compact();
    }

    public static void validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }
    }
}
