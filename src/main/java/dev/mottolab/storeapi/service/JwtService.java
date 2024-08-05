package dev.mottolab.storeapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${config.jwt.secretKey}")
    private String secretKey;
    @Value("${config.jwt.issuer}")
    private String issuer;

    public String createAccessToken(String account){
        long now = System.currentTimeMillis();

        return JWT.create()
                .withClaim("account", account)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + 1000 * 60 * 60 * 24 * 7))
                .withIssuer(this.issuer)
                .sign(this.getAlgorithm());
    }

    public String getUserId(String token){
        return this.decode(token).getClaim("account").asString();
    }

    private DecodedJWT decode(String token) throws  JWTVerificationException {
        JWTVerifier verifier = JWT.require(this.getAlgorithm())
                .withIssuer(this.issuer)
                .build();
        return verifier.verify(token);
    }

    private Algorithm getAlgorithm(){
        return Algorithm.HMAC384(secretKey);
    }
}
