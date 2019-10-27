package com.dknapik.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.model.Account;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private AccountRepository accountRepository;
	protected final Logger log = LogManager.getLogger(getClass().getName()); 
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
			AccountRepository accountRepository) {
		super(authenticationManager);
		this.accountRepository = accountRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//Read the Authorization header, where the JWT token should be
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		
		// If header does not contain BEARER or is null delegate exit
		if ((header == null) || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		// If header is present, try to grab user principal from database and perform authoritzation
		Authentication authentication;
		authentication = getUsernamePasswordAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
	}
	
	private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
		String token = request.getHeader(JwtProperties.HEADER_STRING);
		String userName = null;
		if (token != null) {
			//Parse token and decode it
			userName = JWT.require(JwtProperties.ENCODING_ALGORITHM).build().verify(token.replace(JwtProperties.TOKEN_PREFIX, "")).getSubject();
			
			//Validate that user exists in database
			if (userName != null) {
				Account account = accountRepository.findByName(userName).orElse(null);
				UserPrincipal userPrincipal = new UserPrincipal(account);
				return new UsernamePasswordAuthenticationToken(userName, null, userPrincipal.getAuthorities());
			}
		}
		return null;
	}
}
