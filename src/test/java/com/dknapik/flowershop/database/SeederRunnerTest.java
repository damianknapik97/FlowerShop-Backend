package com.dknapik.flowershop.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This test uses DatabaseSeeder with Debug Mode set in ApplicationContext, to check if
 * physical PostgreSQL database will be seeded without any errors.
 *
 * Queries are checked on H2 Database in other test classes.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SeederRunnerTest {

    @Test
    public void seedPostgreSQLDatabase() { }
}
