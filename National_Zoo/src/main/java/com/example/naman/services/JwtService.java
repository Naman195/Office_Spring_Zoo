package com.example.naman.services;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.naman.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * JwtSerice Service
 * @author Naman Arora
 *
 * @since 30-dec-2024
  */
@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

//    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration = 6*60*1000;
    
    @Autowired
    private RolesPriviledgesService rolesPriviledgesService;

    /**
     * This method is used for Extract the Username from token
     * @param token
     * @return username
     */
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * this method is used for extract the Claims from Token
     * @param <T>
     * @param token
     * @param claimsResolver
     * @return 
     */

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * this Metod is used for Putting userDetails in the token as PayLoad and internally it uses Generate TokenMethod.
     * @param userDetails
     * @return generateToken(mp, userDetails);
     */

    public String generateToken(User userDetails) {
    	Map<String, Object> mp = new HashMap<>();
    	mp.put("username", userDetails.getUsername());
    	mp.put("userId", userDetails.getUserId());
    	mp.put("role", userDetails.getRole().getRoleName());
    	mp.put("authorities", rolesPriviledgesService.getPriviledgeForRole(userDetails.getRole()).stream().map(x -> x.getAuthority()).collect(Collectors.toList()));
        return generateToken(mp, userDetails);
    }
 
    
    /**
     * this method is used for Generate the token 
     * @param extraClaims
     * @param userDetails
     * @return
     */
    
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    
    /**
     * this method is used for extract the Jwt  expiration Time
     * @return JWtExpiration
     */
    
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * this method is used for build the jWt token
     * @param extraClaims
     * @param userDetails
     * @param expiration
     * @return
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    
    /**
     * this method is used for check the JWt token validity
     * @param token
     * @return Boolean Output
     */
    
    public boolean isTokenValid(String token) {
       
        return !isTokenExpired(token);
    }

    /**
     * check token Expire or not 
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * This method is used for extarct the Expiration from JWT
     * @param token
     * @return
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * this Method is used for getExp time 
     * @param token
     * @return
     */
    public LocalDateTime getExp(String token) {
        Claims xy = extractAllClaims(token);
        Long expTimeStamp = xy.getExpiration().getTime()/1000;
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(expTimeStamp), ZoneId.systemDefault());
    }
    
    /**
     * this method is used for Extract all the Claims from token
     * @param token
     * @return
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * this method is used for to get the SignIn Key for Build the JWT token
     * @return
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}