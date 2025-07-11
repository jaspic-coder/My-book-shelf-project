package org.example.mybooklibrary.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.mybooklibrary.user.Role;
import org.example.mybooklibrary.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.expiration}")
    private long expiration;

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public User extractUserDetails(String token) {
        String email = getEmailFromToken(token);
        String role = getRoleFromToken(token);

        User user = new User();
        user.setEmail(email);
        user.setRole(Role.valueOf(role));
        return user;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
