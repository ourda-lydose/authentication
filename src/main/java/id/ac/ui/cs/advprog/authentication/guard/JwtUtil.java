package id.ac.ui.cs.advprog.authentication.guard;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {
    public static String extractUsername(String token) {
        String subject = extractClaim(token, Claims::getSubject);
        return subject;
    }

    public static String extractEntityType(String token){
        final Claims claims=extractAllClaims(token);
        return (String) claims.get(JwtConstants.ENTITY_TYPE);
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {

        Claims parsedClaims = Jwts.parser().setSigningKey(JwtConstants.SECRET_KEY).parseClaimsJws(token).getBody();
        return parsedClaims;
    }

    private static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static String generateToken(String username, String entityType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.ENTITY_TYPE,entityType);
        return createToken(claims, username);
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        Date issueDate = new Date(System.currentTimeMillis());

        Date expirationDate = new Date(System.currentTimeMillis() + JwtConstants.EXPIRATION_TIME
        );
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(issueDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, JwtConstants.SECRET_KEY).compact()
                ;
    }


    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isUsernameValid = username.equals(userDetails.getUsername());
        boolean isJwtTtokenExpired = isTokenExpired(token);
        System.out.println("Is token expired: " + isJwtTtokenExpired + " is username valid: " + isUsernameValid);
        if (!isUsernameValid) {
        }
        if (isJwtTtokenExpired) {
        }
        return (isUsernameValid && !isJwtTtokenExpired);
    }

    public static String[] decodedBase64(String token) {

        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String pairedCredentials = new String(decodedBytes);

        return pairedCredentials.split(":", 2);

    }
}