package com.dknapik.flowershop.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
final class MoneyUtilsTest {
    @Autowired
    private MoneyUtils moneyUtils;
    @Autowired
    private Environment environment;

    @Test
    void getApplicationCurrencyUnitTest() {
        CurrencyUnit retrievedCurrencyUnit = moneyUtils.getApplicationCurrencyUnit();
        CurrencyUnit expectedCurrencyUnit = Monetary.getCurrency(environment.getProperty("app-monetary-currency"));
        Assertions.assertThat(retrievedCurrencyUnit).isEqualTo(expectedCurrencyUnit);
    }
}
