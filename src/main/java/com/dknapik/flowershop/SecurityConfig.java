package com.dknapik.flowershop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.security.JwtAuthenticationFilter;
import com.dknapik.security.JwtAuthorizationFilter;
import com.dknapik.security.UserPrincipalDetailsService;
import com.dknapik.security.UserRoles;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private AccountRepository accRepository;

	public SecurityConfig(AccountRepository accRepository) {
		this.accRepository = accRepository;
	}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }
	
    @Override
    protected void configure(HttpSecurity security) throws Exception {
    	//JWT doesn't require session or csrf
    	security.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    			.and()
    			.csrf().disable()
    			.cors()
    			.and()
    			.addFilter(new JwtAuthenticationFilter(authenticationManager()))
    			.addFilter(new JwtAuthorizationFilter(authenticationManager(), this.accRepository))
    			.authorizeRequests()
    			.antMatchers("/login").permitAll()
    			.antMatchers("/account/register").permitAll();
    			//.anyRequest().authenticated();
    			
    }
    
    @Bean
	protected UserDetailsService userDetailsService() {
    	UserPrincipalDetailsService userPrincipalDetailsService = new UserPrincipalDetailsService(accRepository);
    	
    	return userPrincipalDetailsService;
    }
    
    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());

        return daoAuthenticationProvider;
    }
    
    @Bean("PasswordEncoder")
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}