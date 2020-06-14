package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.AccountRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    void saveToDatabaseTest() {
        /* Create new entity */
        Account account =
                new Account("TEST_NAME", "Test_Password", "Test_mail@test.pl", AccountRole.ROLE_EMPLOYEE);

        /* Save new entity to database using repository */
        accountRepository.saveAndFlush(account);

        /* Retrieve newly saved entity using Test Entity Manager and confirm that retrieved object is the same */
        Account retrievedEntity = entityManager.find(Account.class, account.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(account);
    }

    /**
     * Check if entity can be retrieved from database
     */
    @Test
    void retrieveFromDatabaseTest() {
        /* Create new entity */
        Account account =
                new Account("TEST_NAME", "Test_Password", "Test_mail@test.pl", AccountRole.ROLE_EMPLOYEE);

        /* Save new entity to database with Test Entity Manager */
        entityManager.persistAndFlush(account);

        /* Retrieve newly saved entity using REPOSITORY and check if it matches the one saved */
        Optional<Account> retrievedEntity = accountRepository.findById(account.getId());
        Assertions.assertThat(retrievedEntity.get()).isEqualToComparingFieldByField(account);
    }

    /**
     * Check if entity can retrieved from database using only name parameter
     */
    @Test
    void findByNameTest() {
        /* Create new entity */
        String name = "TEST_NAME";
        Account account =
                new Account(name, "Test_Password", "Test_mail@test.pl", AccountRole.ROLE_EMPLOYEE);

        /* Save new entity to database with Test Entity Manager */
        entityManager.persistAndFlush(account);

        /* Retrieve newly saved entity using REPOSITORY findByName method and compare if it matches the one saved */
        Optional<Account> retrievedEntity = accountRepository.findByName(name);
        Assertions.assertThat(retrievedEntity.get()).isEqualToComparingFieldByField(account);
    }

    /**
     * Check if entity exists in database using only name parameter.
     */
    @Test
    void existsByNameTest() {
        /* Create new entity */
        String name = "TEST_NAME";
        Account account =
                new Account(name, "Test_Password", "Test_mail@test.pl", AccountRole.ROLE_EMPLOYEE);

        /* Save new entity to database with Test Entity Manager */
        entityManager.persistAndFlush(account);

        /* Check if saved entity exists in database using REPOSITORY existsByName method */
        boolean existsInDatabase = accountRepository.existsByName(name);
        Assertions.assertThat(existsInDatabase).isTrue();
    }
}
