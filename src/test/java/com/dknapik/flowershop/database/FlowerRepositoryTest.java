package com.dknapik.flowershop.database;

import com.dknapik.flowershop.PostgreSQLDatabase;
import com.dknapik.flowershop.model.Flower;
import org.javamoney.moneta.Money;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class FlowerRepositoryTest {
    private FlowerRepository flowerRepository;
    private Environment env;
    private PostgreSQLContainer postgres = new PostgreSQLContainer<>();
    @Value("${app-monetary-currency}")
    private CurrencyUnit currency;

    @ClassRule
    public static PostgreSQLContainer database = PostgreSQLDatabase.getInstance();

    @Test
    public void simpleTest() {

    }

}
