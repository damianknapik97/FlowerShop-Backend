package com.dknapik.flowershop.utils;

import lombok.ToString;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

@Service
@ToString
public class MoneyUtils {
    private final CurrencyUnit currencyUnit;


    public MoneyUtils(Environment env) {
        this.currencyUnit = Monetary.getCurrency(env.getProperty("app-monetary-currency"));
    }

    public CurrencyUnit getApplicationCurrencyUnit() {
        return currencyUnit;
    }

}
