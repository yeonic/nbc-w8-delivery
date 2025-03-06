package com.ateen.delivery.domain.auth;

import com.ateen.delivery.domain.user.constants.UserType;
import com.ateen.delivery.global.constants.KeyConst;
import io.jsonwebtoken.Claims;
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

    public String createAccessToken(Long userId, String email, UserType userType) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim(KeyConst.JWT_CLAIM_EMAIL, email)
                .claim(KeyConst.JWT_CLAIM_USER_TYPE, userType)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
                .signWith(secretKey)
                .compact();
    }

    public Claims getJwtPayload(String authHeader) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(trimBearer(authHeader))
                .getPayload();
    }

    public boolean verifyToken(String authHeader) {
        try {
            Jwts.parser().verifyWith(secretKey)
                    .build().parseSignedClaims(trimBearer(authHeader));
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private String trimBearer(String bearer) {
        if (!bearer.contains("Bearer")) {
            return bearer;
        }
        return bearer.replace("Bearer ", "");
    }
}
