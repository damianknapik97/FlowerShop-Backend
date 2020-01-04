package com.dknapik.flowershop.database.product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This test uses DatabaseSeeder with Debug Mode set in ApplicationContext, to check if
 * physical PostgreSQL database will be seeded without any errors.
 * <p>
 * Queries are checked on H2 Database in other test classes.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"app-debug-mode=true"})  // Turn on debug mode so seeder will initialize all test data
public class SeederRunnerTest {

    @Test
    public void seedPostgreSQLDatabase() {
    }
}
