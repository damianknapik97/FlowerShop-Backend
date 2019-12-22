package com.dknapik.flowershop.utility;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

@Service
public class MoneyUtils {
    private final String applicationCurrency;


    public MoneyUtils(Environment env) {
        applicationCurrency = env.getProperty("app-monetary-currency");
    }

    public CurrencyUnit getApplicationCurrencyUnit() {
        return Monetary.getCurrency(applicationCurrency);
    }

}
