package com.dknapik.security;

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
import com.auth0.jwt.algorithms.Algorithm;
import com.dknapik.flowershop.api.viewmodel.LoginViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	protected final Logger log = LogManager.getLogger(getClass().getName());
	private AuthenticationManager authenticationManager;


	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}


	/**
	 * Triggered when issued post request to /login
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		LoginViewModel credentials = null;
		try {
			//Map request to LoginViewModel, which contains credentials and data validation
			credentials = new ObjectMapper().readValue(request.getInputStream(), LoginViewModel.class);
			
			//Create login token
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), new ArrayList<>());
			
			//Authenticate user
			return authenticationManager.authenticate(authenticationToken);
		} catch(IOException e) {
			e.printStackTrace();
			log.warn("Couldn't map login credentials to LoginViewModel class");
		}
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		//Grab pricinpal
		UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
		
		//Create JWT token
		String token = JWT.create()
				.withSubject(principal.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.sign(JwtProperties.ENCODING_ALGORITHM);
		
		//Add JWT token in response
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
		//Add Role in response
		response.addHeader("Role", principal.toString());
		//Add Account ID in response
		response.addHeader("ID", principal.getID().toString());
		//Add CORS policy header
		response.addHeader("Access-Control-Expose-Headers", "Authorization, Role, ID");
		response.addHeader("Access-Control-Allow-Origin", "*");
	}
	
	
	

}
