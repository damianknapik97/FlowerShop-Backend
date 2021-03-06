package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.AccountRole;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Class for seeding database accounts containing certain privileges.
 *
 * @author Damian
 */
@ToString
@Component
class AccountSeeder implements SeederInt {
    private ApplicationContext applicationContext;      // To retrieve application password encoder
    private AccountRepository accountRepository;        // To save/retrieve entities from database
    private static final boolean ONLY_FOR_DEBUG = false;  // To check if class should be always instantiated and used


    @Autowired
    AccountSeeder(ApplicationContext applicationContext, AccountRepository accountRepository) {
        this.applicationContext = applicationContext;
        this.accountRepository = accountRepository;
    }

    @Override
    public void seed() {
        PasswordEncoder encoder = applicationContext.getBean(PasswordEncoder.class);

        /* Check if basic accounts are already in database, and initialize if not */
        if (!this.accountRepository.existsByName("root")) {
            accountRepository.save(new Account("root",
                    encoder.encode("root"),
                    "root@test.pl",
                    AccountRole.ROLE_ADMIN));
        }

        if (!this.accountRepository.existsByName("employee")) {
            accountRepository.save(new Account("employee",
                    encoder.encode("employee"),
                    "employee@test.pl",
                    AccountRole.ROLE_EMPLOYEE));
        }

        this.accountRepository.flush();
    }

    @Override
    public boolean isOnlyForDebug() {
        return ONLY_FOR_DEBUG;
    }
}
