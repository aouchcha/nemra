package backend.nemra.shared.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class jwtUtils {
    private final Key secretKey;
    private final long AccessTokenExpiresIn;
    private final long RefreshExpiresIn;

    public jwtUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token.expiration}") long AccessTokenExpiresIn,
            @Value("${jwt.refresh-token.expiration}") long RefreshExpiresIn
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.AccessTokenExpiresIn = AccessTokenExpiresIn;
        this.RefreshExpiresIn = RefreshExpiresIn;
    }

    public String generateAccessToken(UUID uuid, String role) {
        return Jwts.builder()
                .subject(uuid.toString())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + AccessTokenExpiresIn))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(UUID uuid) {
        return Jwts.builder()
                .subject(uuid.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + RefreshExpiresIn))
                .signWith(secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String barerToken = request.getHeader("Authorization");
        return extractToken(barerToken);
    }

    public static String extractToken(String barerToken) {
        if (barerToken != null && barerToken.startsWith("Bearer ")) {
            return barerToken.replace("Bearer ", "");
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) return false;
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUuid(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}