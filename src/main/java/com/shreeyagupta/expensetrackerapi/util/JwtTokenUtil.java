
package com.shreeyagupta.expensetrackerapi.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {

    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        SecretKey key = Keys.hmacShaKeyFor(
                secret.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(
                        System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000
                ))
                .signWith(key)
                .compact();
    }
    
    public String getUsernameFromToken(String jwtToken) {
    	return getClaimFromToken(jwtToken,Claims::getSubject);
    }
    
    private <T> T getClaimFromToken(String token,Function<Claims,T>claimsResolver) {
    	//final Claims claims=Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    	SecretKey key = Keys.hmacShaKeyFor(
                secret.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );

        final Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    	return claimsResolver.apply(claims);
    }
    
    public boolean validateToken(String jwtToken,UserDetails userDetails) {
    	final String username=getUsernameFromToken(jwtToken);
    	return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }
    private boolean isTokenExpired(String jwtToken) {
    	final Date expiration=getExpirationDateFromToken(jwtToken);
    	return expiration.before(new Date());
    }
    private Date getExpirationDateFromToken(String jwtToken) {
    	return getClaimFromToken(jwtToken,Claims::getExpiration);
    }
}
