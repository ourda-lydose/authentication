package id.ac.ui.cs.advprog.authentication.guard;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUtil {
    @Value("${security.jwt.secret-key}")
    private static String jwtSecretKey;

    @Value("${security.jwt.expiration-time}")
    private static long jwtExpirationTime;
    public static String extractUsername(String token) {
        String subject = extractClaim(token, Claims::getSubject);
        return subject;
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        Claims parsedClaims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
        return parsedClaims;
    }

    private static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static String generateToken(UserDetails userDetails, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, userDetails);
    }

    public static String createToken(Map<String, Object> claims, UserDetails userDetails) {
        Date issueDate = new Date(System.currentTimeMillis());

        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationTime
        );
        return Jwts.builder().setClaims(claims).setSubject(String.valueOf(userDetails)).setIssuedAt(issueDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256,  jwtSecretKey).compact()
                ;
    }
}