package com.dknapik.flowershop.utility;

import com.dknapik.flowershop.utility.MoneyUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.security.SecureRandom;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class MoneyUtilsTest {
    @Autowired
    private MoneyUtils moneyUtils;
    @Autowired
    private Environment environment;

    @Test
    public void getApplicationCurrencyUnitTest() {
        CurrencyUnit retrievedCurrencyUnit = moneyUtils.getApplicationCurrencyUnit();
        CurrencyUnit expectedCurrencyUnit = Monetary.getCurrency(environment.getProperty("app-monetary-currency"));
        Assertions.assertThat(retrievedCurrencyUnit).isEqualTo(expectedCurrencyUnit);
    }
}
