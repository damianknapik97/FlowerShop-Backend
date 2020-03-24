package com.dknapik.flowershop;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.security.JwtAuthenticationFilter;
import com.dknapik.flowershop.security.JwtAuthorizationFilter;
import com.dknapik.flowershop.security.UserPrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Standard Spring security config.
 * Defines configuration for:
 * -user Authentication
 * -user Authorization
 * -cors policy
 * -password encryption method
 *
 * @author Damian
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private AccountRepository accRepository; // Stores user login and passwords

    @Autowired
    public SecurityConfig(AccountRepository accRepository) {
        this.accRepository = accRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    /**
     * Main overall spring security configuration method
     */
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // JWT doesn't require session
                .and()
                .csrf().disable()                                                            // JWT doesn't require csrf
                .cors()                                                                      // Needed to use CORS bean
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))                     // Defining method of authentication
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.accRepository))  // Defining method of authorization
                .authorizeRequests()  // Turning on the authorization
                .antMatchers("/login").permitAll()
                .antMatchers("/account").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/product/**").permitAll()
                .anyRequest().authenticated();  // Restricting user access to api's besides ones defined above
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        return new UserPrincipalDetailsService(accRepository);
    }

    /**
     * Configure custom Authentication Provider
     *
     * @return
     */
    @Bean
    protected DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());

        return daoAuthenticationProvider;
    }

    /**
     * Configure bean that will be used to encrypt or compare user provided passwords
     *
     * @return
     */
    @Bean(name = "PasswordEncoder")
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}