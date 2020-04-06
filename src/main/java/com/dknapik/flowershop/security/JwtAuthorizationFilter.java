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
import java.util.Optional;

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
     * Filter user request for existing JWT Token and correct assigned Role.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.traceEntry();

        // If header is present, try to grab user principal from database and perform authoritzation.
        if (checkIfHeadersArePresent(request)) {
            Authentication authentication = authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("User authenticated");
        } else {
            log.info("Headers are not present in request, continuing...");
        }
        log.traceExit();
        chain.doFilter(request, response);
    }

    /**
     * Check if headers required for authorization are present.
     * Currently following headers are checked:
     * Token Header
     * Role Header
     */
    private boolean checkIfHeadersArePresent(HttpServletRequest request)
            throws IOException, ServletException {
        log.traceEntry();

        // If token header does not contain BEARER or is null delegate exit
        String header = request.getHeader(JwtProperties.TOKEN_HEADER);
        if ((header == null) || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            log.trace("Token header doesn't exist in request or is invalid");
            return false;
        }
        log.debug("Token Header:" + header);

        // If role header is null delegate exit
        header = request.getHeader(JwtProperties.ROLE_HEADER);
        if (header == null) {
            log.trace("Role header doesn't exist in request");
            return false;
        }
        log.debug("Role Header:" + header);

        //log.debug();

        log.traceExit();
        return true;
    }

    /**
     * Check provided JWT Token and Role validity
     */
    private Authentication authenticate(HttpServletRequest request) {
        log.traceEntry();
        String token = request.getHeader(JwtProperties.TOKEN_HEADER);
        String role = request.getHeader(JwtProperties.ROLE_HEADER);

        System.out.println(role);
        /* Parse token and decode it */
        String userName = JWT.require(JwtProperties.ENCODING_ALGORITHM)
                .build()
                .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""))
                .getSubject();

        /* Check if user name was properly decoded */
        if (userName == null) {
            log.error("Decoded JWT Token is either null or empty");
            return null;
        }

        /* Check if account related to decoded token was successfully found */
        Optional<Account> optionalAccount = accountRepository.findByName(userName);
        if (!optionalAccount.isPresent()) {
            log.error("Couldn't find account matching decoded JWT Token");
            return null;
        }
        Account account = optionalAccount.get();

        /* Validate user role */
        if (!account.getRole().toString().contentEquals(role)) {
            return null;
        }

        /* Headers validated */
        UserPrincipal userPrincipal = new UserPrincipal(account);
        log.traceExit();
        return new UsernamePasswordAuthenticationToken(userName, null, userPrincipal.getAuthorities());

    }
}
