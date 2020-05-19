package com.dknapik.flowershop.utils;

import lombok.ToString;
import org.javamoney.moneta.Money;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

@Service
@ToString
public final class MoneyUtils {
    private final CurrencyUnit currencyUnit;


    public MoneyUtils(Environment env) {
        this.currencyUnit = Monetary.getCurrency(env.getProperty("app-monetary-currency"));
    }

    public CurrencyUnit getApplicationCurrencyUnit() {
        return currencyUnit;
    }

    /**
     * Checks if two provided currency codes match each other
     *
     * @return true if currencies match.
     */
    public boolean checkMatchingCurrencies(CurrencyUnit source, CurrencyUnit target) {
        return source.getNumericCode() == target.getNumericCode();
    }

    /**
     * Creates Money object from provided amount and currently set application currency.
     *
     * @param amount - monetary amount
     * @return - MonetaryAmount interface implemented inside Money class.
     */
    public MonetaryAmount amountWithAppCurrency(Number amount) {
        return Money.of(amount, getApplicationCurrencyUnit());
    }

}
