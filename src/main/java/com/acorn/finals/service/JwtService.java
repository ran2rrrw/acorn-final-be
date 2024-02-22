package com.acorn.finals.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@Profile("jwt")
public class JwtService {
    //@Value("${jwt.name}")

    private final String secretString = "long-longlonglonglonglonglonglonglong-long-long-long-long-long-secret";
    private final String encodedSecretString = Encoders.BASE64.encode(secretString.getBytes(StandardCharsets.UTF_8));
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedSecretString));
    String secret = Encoders.BASE64.encode(key.getEncoded());

    private final String getSecretString2 = "secret-key";


    public String createToken(Map<String, Object> claims, String subject) {

        log.debug(secret); // secret key here

        return Jwts.builder()
                .setClaims(claims)
                .subject(subject)
                .signWith(key)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();
    }

    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        //테스트로 추가 정보도 담아보기
        claims.put("email", "naver@");
        claims.put("addr", "서울시 강남구");
        return createToken(claims, subject);
    }

    public String extractUserName(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public String isTokenExpired(String token) {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
        return null;
    }


    public boolean validateToken(String token) {
        boolean claim;
        String userName = extractUserName(token);
        try {
            var parsedJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            claim = parsedJws.getPayload().getSubject().equals(userName);
            System.out.println(parsedJws.getPayload().getIssuedAt());
            System.out.println(parsedJws.getPayload().getSubject());
            System.out.println(parsedJws.getPayload().getExpiration());
            System.out.println(parsedJws.getPayload().get("email"));
            System.out.println(parsedJws.getPayload().get("addr"));
            System.out.println(Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody());
            //OK, we can trust this JWT

        } catch (JwtException e) {

            claim = false;
        }
        return claim;
    }

}
