package com.dknapik.flowershop.security;

import java.security.SecureRandom;

import com.auth0.jwt.algorithms.Algorithm;

/**
 * Constants used in JWT authentication and authorization
 * 
 * @author Damian
 *
 */
public class JwtProperties {
	public static final byte[] SECRET = SecureRandom.getSeed(16);
	public static final int EXPIRATION_TIME = 864000000;  // 10 days ?
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final Algorithm ENCODING_ALGORITHM = Algorithm.HMAC512(SECRET);
	
}
