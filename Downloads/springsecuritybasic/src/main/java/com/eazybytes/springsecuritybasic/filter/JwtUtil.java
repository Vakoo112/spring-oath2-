/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eazybytes.springsecuritybasic.filter;

import com.eazybytes.springsecuritybasic.model.Authority;
import com.eazybytes.springsecuritybasic.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author vako
 */
@Slf4j
@Component
public class JwtUtil {
   
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.auth.expiration}")
    private long authExp;
    @Value("${jwt.confirm.expiration}")
    private long confirmExp;
    
    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";
    private static final String AUTHENTICATED = "authenticated";
    private static final String AUTHORITIES = "authorities";

    public String generateJwt(User user, boolean authenticated) {
        Map<String, Object> claims = new HashMap();
        claims.put(USER_ID, user.getId());
        claims.put(USERNAME, user.getUsername());
        claims.put(AUTHENTICATED, authenticated);

        long expiration;

        if (authenticated) {
            claims.put(AUTHORITIES, user.getUserAuthorities());
            expiration = System.currentTimeMillis() + authExp;;
        } else {
           
            expiration = System.currentTimeMillis() + confirmExp;
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserId(String jwt) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody()
                .get(USER_ID, Long.class);
    }

    public String getUsername(String jwt) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody()
                .get(USERNAME, String.class);
    }

    public List<String> getUserAuthorities(String jwt) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody()
                .get(AUTHORITIES, List.class);
    }

    public Date getExpirationDate(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean isAuthenticated(String jwt) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody()
                .get(AUTHENTICATED, Boolean.class);
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }
}
