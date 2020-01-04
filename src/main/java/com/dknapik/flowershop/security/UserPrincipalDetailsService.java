package com.dknapik.flowershop.security;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.model.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private final AccountRepository accRepository;

    public UserPrincipalDetailsService(AccountRepository accRepository) {
        this.accRepository = accRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        Account account = this.accRepository.findByName(s).orElse(null);
        return new UserPrincipal(account);
    }
}
