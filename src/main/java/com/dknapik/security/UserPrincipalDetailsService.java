package com.dknapik.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.model.Account;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private AccountRepository accRepository;

    public UserPrincipalDetailsService(AccountRepository accRepository) {
        this.accRepository = accRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = this.accRepository.findByName(s).orElse(null);
        UserPrincipal userPrincipal = new UserPrincipal(account);

        return userPrincipal;
    }
}
