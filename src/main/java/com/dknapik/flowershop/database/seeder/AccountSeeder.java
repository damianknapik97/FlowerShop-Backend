package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.security.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountSeeder implements SeederInt {
    @Autowired
    private ApplicationContext applicationContext;  // To retrieve application password encoder
    @Autowired
    private AccountRepository accountRepository;    // To save/retrieve entities from database
    private static final boolean onlyForDebug = false;    // To check if class should be always instantiated and used


    @Override
    public void seed() {
        PasswordEncoder encoder = applicationContext.getBean(PasswordEncoder.class);

        /* Check if basic accounts are already in database, and initialize if not */
        if (!this.accountRepository.existsByName("root")) {
            accountRepository.save(new Account("root",
                                                encoder.encode("root"),
                                                "root@test.pl",
                                                UserRoles.ADMIN));
        }

        if (!this.accountRepository.existsByName("employee")) {
            accountRepository.save(new Account("employee",
                                                encoder.encode("employee"),
                                                "employee@test.pl",
                                                UserRoles.EMPLOYEE));
        }

        if (!this.accountRepository.existsByName("user")) {
            accountRepository.save(new Account("user",
                                                encoder.encode("user"),
                                                "user@test.pl",
                                                UserRoles.USER));
        }
        this.accountRepository.flush();
    }

    @Override
    public boolean isOnlyForDebug() {
        return onlyForDebug;
    }
}
