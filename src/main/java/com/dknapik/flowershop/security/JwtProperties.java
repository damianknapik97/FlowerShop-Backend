package com.dknapik.flowershop.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.ToString;

import java.security.SecureRandom;

/**
 * Constants used in JWT authentication and authorization
 *
 * @author Damian
 */
@ToString
public class JwtProperties {
    public static final byte[] SECRET = "[B@2bd4308b".getBytes();
    public static final int EXPIRATION_TIME = 864000000;                           // Token is valid for around 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final Algorithm ENCODING_ALGORITHM = Algorithm.HMAC512(SECRET);  // Create new encoding algorithm

}
