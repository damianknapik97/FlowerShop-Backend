package com.dknapik.flowershop.security;

import com.auth0.jwt.JWT;
import com.dknapik.flowershop.dto.account.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@ToString
@Log4j2
public final class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Triggered when issued post request to /login
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        LoginDTO credentials;
        try {
            //Map request to LoginViewModel, which contains credentials and data validation
            credentials = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);

            //Create login token
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(),
                            credentials.getPassword(),
                            new ArrayList<>());

            //Authenticate user
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            log.warn("Couldn't map login credentials to LoginViewModel class");
        }
        return null;
    }

    /**
     * Authentication was successful, generate JWT token and prepare response
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // Grab pricinpal
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        // Create JWT token
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(JwtProperties.ENCODING_ALGORITHM);

        // Add JWT token in response
        response.addHeader(JwtProperties.TOKEN_HEADER, JwtProperties.TOKEN_PREFIX + token);
        // Add Role in response
        response.addHeader(JwtProperties.ROLE_HEADER, principal.getRole().toString());
        // Add CORS policy header
        response.addHeader("Access-Control-Expose-Headers", "Authorization, Role, ID");
    }
}
