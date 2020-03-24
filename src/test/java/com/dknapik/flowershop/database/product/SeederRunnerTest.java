package com.dknapik.flowershop.database.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This test uses DatabaseSeeder with Debug Mode set in ApplicationContext, to check if
 * physical PostgreSQL database will be seeded without any errors.
 * <p>
 * Queries are checked on H2 Database in other test classes.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = {"app-debug-mode=true"})  // Turn on debug mode so seeder will initialize all test data
final class SeederRunnerTest {

    @Test
    void seedPostgreSQLDatabase() {
    }
}
