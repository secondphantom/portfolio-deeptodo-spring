package net.deeptodo.app.application.auth.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import net.deeptodo.app.application.auth.dto.JwtPayload;
import net.deeptodo.app.application.auth.interfaces.TokenType;
import net.deeptodo.app.config.ConfigJwt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final ConfigJwt configJwt;

    public String generateToken(
            LocalDateTime publishTime,
            TokenType tokenType,
            JwtPayload payload) {

        Date targetDate = convertDate(publishTime);
        String key = configJwt.findTokenKey(tokenType);
        Integer expiredHours = configJwt.findExpiredHours(tokenType);

        return Jwts.builder()
                .setIssuedAt(targetDate)
                .setExpiration(new Date(targetDate.getTime() + expiredHours * 60 * 60 * 1000L))
                .addClaims(JwtPayload.covertToClaims(payload))
                .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

    }

    private Date convertDate(final LocalDateTime targetTime) {
        final Instant targetInstant = targetTime.atZone(ZoneId.of("Asia/Seoul"))
                .toInstant();

        return Date.from(targetInstant);
    }


    public Optional<JwtPayload> validateToken(TokenType tokenType, String token) {
        String key = configJwt.findTokenKey(tokenType);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();

            return Optional.of(JwtPayload.of(claims.get("userId", Long.class)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public Integer getMaxAge(TokenType tokenType) {
        return configJwt.findExpiredHours(tokenType) * 60 * 60;
    }
}
