package x.none.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    public static final long JWT_TOKEN_VALIDITY = 300000;
    private static final String ROLES_CLAIM = "roles";
    @Value("${jwt.token}")
    private String secret;


    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public List<String> getRoles(String token) {
        Claims allClaims = getAllClaimsFromToken(token);
        List<String> roles = allClaims.get(ROLES_CLAIM, List.class);

        return roles;
    }

    public String generateToken(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES_CLAIM, roles);
        return doGenerateToken(claims, userDetails.getUsername());
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException |
                 UnsupportedJwtException e) {
            throw new RuntimeException(e);
        }
    }


    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims allClaims = getAllClaimsFromToken(token);
        return claimsResolver.apply(allClaims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
