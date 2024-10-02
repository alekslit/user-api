package ru.telros.uapi.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.telros.uapi.exception.JwtValidationException;
import ru.telros.uapi.user.GenerateUserTokenDto;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static ru.telros.uapi.exception.JwtValidationException.*;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(GenerateUserTokenDto user) {
        final LocalDateTime now = LocalDateTime.now();
        // для упрощения составления авто-тестов взяли длительный срок экспирации токена:
        final Instant accessExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .subject(user.getEmail())
                .expiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("roles", user.getRoles())
                .claim("id", user.getId())
                .compact();
    }

    public String generateRefreshToken(GenerateUserTokenDto user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .subject(user.getEmail())
                .expiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    private boolean validateToken(String token, SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parse(token);
            return true;

        } catch (ExpiredJwtException expJwtEx) {
            log.debug("{}: {}", expJwtEx.getClass().getSimpleName(), EXPIRED_JWT_MESSAGE);
            throwJwtValidationException(EXPIRED_JWT_MESSAGE);
        } catch (MalformedJwtException malJwtEx) {
            log.debug("{}: {}", malJwtEx.getClass().getSimpleName(), MALFORMED_JWT_MESSAGE);
            throwJwtValidationException(MALFORMED_JWT_MESSAGE);
        } catch (SignatureException sigEx) {
            log.debug("{}: {}", sigEx.getClass().getSimpleName(), SIGNATURE_EXCEPTION_MESSAGE);
            throwJwtValidationException(SIGNATURE_EXCEPTION_MESSAGE);
        } catch (SecurityException secEx) {
            log.debug("{}: {}", secEx.getClass().getSimpleName(), secEx.getMessage());
            throwJwtValidationException(secEx.getMessage());
        }

        return false;
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private Claims getClaims(String token, SecretKey secret) {
        return (Claims) Jwts.parser()
                .verifyWith(secret)
                .build()
                .parse(token)
                .getPayload();
    }

    public Claims getAccessClaims(String accessToken) {
        return getClaims(accessToken, jwtAccessSecret);
    }

    public Claims getRefreshClaims(String refreshToken) {
        return getClaims(refreshToken, jwtRefreshSecret);
    }

    private void throwJwtValidationException(String message) {
        throw new JwtValidationException(JWT_VALIDATION_EXCEPTION_REASON, message);
    }
}