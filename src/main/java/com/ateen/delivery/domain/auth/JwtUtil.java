package com.ateen.delivery.domain.auth;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

//    public static final long ACCESSTOKEN_TIME = 1000 * 60 * 30; // 30분
//    public static final long REFRESHTOKEN_TIME = 1000 * 60 * 30 * 24 * 14; // 2주

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(this.jwtProperties.getSecretKey().getBytes());
    }

    public String createAccessToken(String email) {
        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        String trimToken = trimBearer(token);
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireExpiration(new Date(System.currentTimeMillis()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean verifyToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey)
                    .requireExpiration(new Date(System.currentTimeMillis()))
                    .build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String trimBearer(String bearer) {
        if (!bearer.contains("Bearer")) {
            return bearer;
        }
        return bearer.replace("Bearer ", "");
    }
}
