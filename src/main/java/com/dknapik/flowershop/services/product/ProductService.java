package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.model.product.Product;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;

@Service
@ToString
@Log4j2
public class ProductService {
    private final MoneyUtils moneyUtils;

    @Autowired
    public ProductService(MoneyUtils moneyUtils) {
        this.moneyUtils = moneyUtils;
    }

    /**
     * Counts total price based on provided product price and number of products. If provided product argument is null
     * then MonetaryAmount containing zero and application currency is returned.
     *
     * @param numberOfProducts - how many times should product price be multiplied
     * @param product          - product entity containing price.
     * @return product price multiplied by number of products, with product currency or zero with application currency.
     */
    public MonetaryAmount countTotalPrice(int numberOfProducts, @Nullable Product product) {
        log.traceEntry();

        if (product == null || product.getUnitPrice() == null) {
            return moneyUtils.zeroWithApplicationCurrency();
        }

        MonetaryAmount totalPrice = product.getUnitPrice().multiply(numberOfProducts);

        log.traceExit();
        return totalPrice;
    }
}
