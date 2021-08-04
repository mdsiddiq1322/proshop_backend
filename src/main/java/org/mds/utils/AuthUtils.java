package org.mds.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

public class AuthUtils {
    static byte[] secret = Base64.getDecoder().decode("5f6c6a7a5992b4f995cc69149f390603947c8f72573288bf2888c7e700cc5ff0a2979cfd8e0cb05ef6af5b3e644b0775a90bda19a659bd3fa4f5a82fd255cd7c");

    static byte[] adminSecret = Base64.getDecoder().decode("f72573288bf2888c7e700cb05ef6af5b3e5f6c6a7a5992b4f995cc69149f390603947c8644b0775a90bda19a659bd3fa4f5a82fd255cd7ccc5ff0a2979cfd8e0");

    static Instant now = Instant.now();

    public static String generateToken(HashMap<Object, Object> payload){
        StringBuilder payloadString = new StringBuilder("{");
        for(HashMap.Entry<Object, Object> pair: payload.entrySet()){
            payloadString.append("\"").append(pair.getKey()).append("\": \"").append(pair.getValue()).append("\"");
        }
        payloadString.append("}");
        return Jwts.builder()
                .claim("token", payloadString.toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(86400)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
    }
    
    public static String generateAdminToken(HashMap<Object, Object> payload){
        StringBuilder payloadString = new StringBuilder("{");
        for(HashMap.Entry<Object, Object> pair: payload.entrySet()){
            payloadString.append("\"").append(pair.getKey()).append("\": \"").append(pair.getValue()).append("\"");
        }
        payloadString.append("}");
        return Jwts.builder()
                .claim("token", payloadString.toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(86400)))
                .signWith(Keys.hmacShaKeyFor(adminSecret))
                .compact();
    }

    public static String verifyToken(String token){
        Jws<Claims> jws = null;
        try {
            jws = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
        } catch (JwtException ex) {
            System.out.println(ex.getMessage());
        }
        if(jws != null){
            return jws.toString();
        } else {
            return null;
        }
    }
    
    public static String verifyAdminToken(String token){
        Jws<Claims> jws = null;
        try {
            jws = Jwts.parserBuilder().setSigningKey(adminSecret).build().parseClaimsJws(token);
        } catch (JwtException ex) {
            System.out.println(ex.getMessage());
        }
        if(jws != null){
            return jws.toString();
        } else {
            return null;
        }
    }
}
