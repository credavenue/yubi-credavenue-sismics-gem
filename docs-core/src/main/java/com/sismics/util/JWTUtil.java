package com.sismics.util;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sismics.Exceptions.ExpiredJWTException;
import com.sismics.Exceptions.InvalidJWTException;
import io.jsonwebtoken.*;


public class JWTUtil {
    private final static String TOKEN_ISSUER = "TEEDY";
    private final static String ISSUER_KEY = "iss";
    private final static String CLAIMS_KEY = "claims";

    // error messages
    private final static String INVALID_TOKEN_ERROR = "JWT token is invalid";
    private final static String EXPIRED_TOKEN_ERROR = "JWT token is expired";

    /**
     * Generate a JWT token with HS256 signature algo
     *
     * @param info             a key value pair to be signed as the part of token
     * @param expiresInSeconds if given 0 or -ve then considering to generate a lifetime token
     * @param secretKey        a key to sign with, should be greater than 256 bit
     * @return JWT token (type: string)
     */
    public static String generateToken(Map<String, Object> info, String secretKey, long expiresInSeconds) {
        // taking HS256 for signing algo, key has to be greater than 256 bit
        var signAlgo = SignatureAlgorithm.HS256;
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(secretBytes, signAlgo.getJcaName());

        long currentMilliseconds = System.currentTimeMillis();
        Date currentDate = new Date(currentMilliseconds);
        var claims = new HashMap<String, Map<String, Object>>();
        claims.put(CLAIMS_KEY, info);

        JwtBuilder builder = Jwts.builder();
        builder.setClaims(claims);
        builder.setIssuedAt(currentDate);
        builder.setIssuer(TOKEN_ISSUER);
        builder.signWith(signingKey);

        // setting expiration only if provided, lifetime otherwise
        if (expiresInSeconds > 0) {
            long expiresInMS = currentMilliseconds + (expiresInSeconds * 1000);
            Date expirationDate = new Date(expiresInMS);
            builder.setExpiration(expirationDate);
        }

        // generate and return JWT token
        return builder.compact();
    }

    /**
     * Verify token and get infoMap
     *
     * @param jwtToken  signed JWT token
     * @param secretKey a key which with token was signed with, should be greater than 256 bit
     * @return infoMap (type: Map<String, Object>)
     */
    public static Map<String, Object> verifyToken(String jwtToken, String secretKey) throws ExpiredJWTException, InvalidJWTException {
        try {
            byte[] secretBytes = DatatypeConverter.parseBase64Binary(secretKey);
            JwtParserBuilder builder = Jwts.parserBuilder();
            JwtParser jwtParser = builder.setSigningKey(secretBytes).build();
            var body = jwtParser.parseClaimsJws(jwtToken).getBody();

            String issuer = (String) body.get(ISSUER_KEY);
            if (!issuer.equals(TOKEN_ISSUER)) {
                throw new InvalidJWTException(INVALID_TOKEN_ERROR, null);
            }

            // if not a type of Map<String, Object> then considering as invalid token
            return (Map<String, Object>) body.get(CLAIMS_KEY);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJWTException(EXPIRED_TOKEN_ERROR, ex);
        } catch (Exception ex) {
            throw new InvalidJWTException(INVALID_TOKEN_ERROR, ex);
        }
    }
}
