package hawa.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtConverter {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final String ISSUER = "Hawa";

    private final int EXPIRATION_MILLISECONDS = 15 * 60 * 1000;

    public String getTokenFromAppUser(User appUser) {
        String roles = appUser.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(appUser.getUsername())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLISECONDS))
                .signWith(key)
                .compact();
    }

    public User getAppUserFromToken(String token) {

        if (token == null) {
            return null;
        }

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            String username = jws.getBody().getSubject();
            String roleStr = (String) jws.getBody().get("roles");
            List<GrantedAuthority> roles = Arrays.stream(roleStr.split(","))
                    .map(r -> new SimpleGrantedAuthority(r))
                    .collect(Collectors.toList());

            return new User(username, username, roles);

        } catch (JwtException | NumberFormatException e) {
            e.printStackTrace();
        }

        return null;
    }

}
