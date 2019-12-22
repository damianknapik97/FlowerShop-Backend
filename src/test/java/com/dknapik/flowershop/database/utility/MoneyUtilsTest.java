package com.dknapik.flowershop.database.utility;

import com.dknapik.flowershop.utility.MoneyUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class MoneyUtilsTest {
    @Autowired
    private MoneyUtils moneyUtils;

    @Test
    public void getApplicationCurrencyUnitTest() {
        CurrencyUnit retrievedCurrencyUnit = moneyUtils.getApplicationCurrencyUnit();
        CurrencyUnit expectedCurrencyUnit = Monetary.getCurrency("PLN");
        Assertions.assertThat(retrievedCurrencyUnit).isEqualTo(expectedCurrencyUnit);
    }
}
