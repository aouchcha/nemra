package backend.nemra.shared.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class jwtUtils {
    private final Key secretKey;
    private final long expiresIn;

    public jwtUtils(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiresIn) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiresIn = expiresIn;
    }

    public String generateToken(UUID uuid, String role) {
        return Jwts.builder()
                .setSubject(uuid.toString())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiresIn))
                .signWith(secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String barerToken = request.getHeader("Authorization");
        if (barerToken != null && barerToken.startsWith("Bearer ")) {
            return barerToken.replace("Bearer ", "");
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) return false;
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUuid(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}