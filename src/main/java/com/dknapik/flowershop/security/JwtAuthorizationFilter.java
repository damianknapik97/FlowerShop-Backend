package com.dknapik.flowershop.security;

import com.auth0.jwt.JWT;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.model.Account;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ToString
@Log4j2
public final class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final AccountRepository accountRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  AccountRepository accountRepository) {
        super(authenticationManager);
        this.accountRepository = accountRepository;
    }

    /**
     * Filter user request for existing JWT Token
     */
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

    /**
     * Check provided JWT Token validity
     */
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
