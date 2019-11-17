package com.dknapik.flowershop.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.dknapik.flowershop.dto.account.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final Logger log = LogManager.getLogger(getClass().getName());
	private final AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Triggered when issued post request to /login
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		LoginDto credentials = null;
		try {
			//Map request to LoginViewModel, which contains credentials and data validation
			credentials = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
			
			//Create login token
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(credentials.getUsername(),
															credentials.getPassword(),
															new ArrayList<>());
			
			//Authenticate user
			return authenticationManager.authenticate(authenticationToken);
		} catch(IOException e) {
			log.warn("Couldn't map login credentials to LoginViewModel class");
		}
		return null;
	}

	/**
	 *  Authentication was successful, generate JWT token and prepare response
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		//Grab pricinpal
		UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
		
		//Create JWT token
		String token = JWT.create()
				.withSubject(principal.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.sign(JwtProperties.ENCODING_ALGORITHM);
		
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);			// Add JWT token in response
		response.addHeader("Role", principal.toString());												// Add Role in response
		response.addHeader("ID", principal.getID().toString());											// Add Account ID in response
		response.addHeader("Access-Control-Expose-Headers", "Authorization, Role, ID");					// Add CORS policy header
	}
}