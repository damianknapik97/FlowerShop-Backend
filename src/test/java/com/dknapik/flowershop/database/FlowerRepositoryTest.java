package com.dknapik.flowershop.database;



import javax.money.CurrencyUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
public class FlowerRepositoryTest {
    private FlowerRepository flowerRepository;
    private Environment env;
    @Value("${app-monetary-currency}")
    private CurrencyUnit currency;


    @Test
    public void simpleTest() {

    }

}
