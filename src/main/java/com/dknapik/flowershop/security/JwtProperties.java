package com.dknapik.flowershop.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.ToString;

/**
 * Constants used in JWT authentication and authorization
 *
 * @author Damian
 */
@ToString
public final class JwtProperties {
    public static final byte[] SECRET = "[B@2bd4308b".getBytes();
    public static final int EXPIRATION_TIME = 864000000;                           // Token is valid for around 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ROLE_HEADER = "Role";
    public static final String TOKEN_HEADER = "Token";
    public static final Algorithm ENCODING_ALGORITHM = Algorithm.HMAC512(SECRET);  // Create new encoding algorithm

}
