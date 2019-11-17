package com.dknapik.flowershop.security;

import java.security.SecureRandom;

import com.auth0.jwt.algorithms.Algorithm;

/**
 * Constants used in JWT authentication and authorization
 * 
 * @author Damian
 * TODO - Change secret generation to user defined or a constant one
 */
public class JwtProperties {
	public static final byte[] SECRET = SecureRandom.getSeed(16); 	   // Generate new secret for each run
	public static final int EXPIRATION_TIME = 864000000; 				    	   // Token is valid for around 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final Algorithm ENCODING_ALGORITHM = Algorithm.HMAC512(SECRET);  // Create new encoding algorithm
	
}
