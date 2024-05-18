package org.peagadev.loadingps2024.domain.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Log4j2
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretString;
    private final Long EXPIRATION_TIME = 24 * 60 * 60 * 1000L;

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuer("loadingps")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(getSecretKey()).compact();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean validToken(String token, UserDetails user) {
        log.info("Validating token:{}" ,token);
        String username = getUsername(token);
        Date expiration = getExpiration(token);
        return username.equals(user.getUsername()) && expiration.after(new Date(System.currentTimeMillis()));
    }

    public String getUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpiration(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    }
}
