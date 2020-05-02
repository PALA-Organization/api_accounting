package fr.pala.accounting.security;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private final long tokenValidityInMilliseconds = Duration.ofMinutes(5).getSeconds() * 1000;
    private final byte[] secret;

    public TokenProvider(@Value("${security.token.secret}") CharSequence secret) {
        this.secret = secret.toString().getBytes();
    }


    public Authentication getAuthentication(String token) {
        Claims claims = parseToken(token).getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            parseToken(authToken);
            return true;

        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            return false;
        }
    }

    private Jws<Claims> parseToken(String authToken) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(authToken);
    }
}