package br.com.microlog.user.communs;

import br.com.microlog.user.models.UserModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtils {
  @Value("${jwt.secret}")
  private String jwtSecret;

  public String getEmailFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String jwt) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(jwt);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String getJwtFromRequest(HttpServletRequest request) {
    String jwt = request.getHeader("Authorization");

    if (jwt != null && !jwt.isEmpty())
      return jwt.substring(7);

    return null;
  }

  public String generateTokenFromUsername(String username) throws InvalidKeyException {
    int jwtExpirationMs = 86400000;

    return Jwts.builder()
      .setSubject(username)
      .setIssuedAt(new Date())
      .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
      .signWith(key(), SignatureAlgorithm.HS256)
      .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtSecret));
  }
}