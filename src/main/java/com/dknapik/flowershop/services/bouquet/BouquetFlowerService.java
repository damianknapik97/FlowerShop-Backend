package com.dknapik.flowershop.services.bouquet;

import com.dknapik.flowershop.database.bouquet.BouquetFlowerRepository;
import com.dknapik.flowershop.model.bouquet.BouquetFlower;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;

@Service
@ToString
@Log4j2
public class BouquetFlowerService {
    private final BouquetFlowerRepository bouquetFlowerRepository;
    private final MoneyUtils moneyUtils;

    @Autowired
    public BouquetFlowerService(BouquetFlowerRepository bouquetFlowerRepository, MoneyUtils moneyUtils) {
        this.bouquetFlowerRepository = bouquetFlowerRepository;
        this.moneyUtils = moneyUtils;
    }

    public MonetaryAmount countTotalPrice(Iterable<BouquetFlower> bouquetFlowerIterable) {
        log.traceEntry();
        MonetaryAmount totalPrice = null;

        if (bouquetFlowerIterable == null) {
            return Money.zero(moneyUtils.getApplicationCurrencyUnit());
        }

        for (BouquetFlower bouquetFlower : bouquetFlowerIterable) {
            if (bouquetFlower != null) {

                /* Check if return value was already initialized */
            }
        }

        log.traceExit();
        return null;
    }
}
