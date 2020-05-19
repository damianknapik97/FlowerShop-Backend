package com.dknapik.flowershop.services.productorder;

import com.dknapik.flowershop.constants.ProductOrderMessage;
import com.dknapik.flowershop.database.order.SouvenirOrderRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.model.productorder.SouvenirOrder;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;

@Service
@Log4j2
@ToString
public final class SouvenirOrderService {
    private final SouvenirOrderRepository repository;
    private final MoneyUtils moneyUtils;

    @Autowired
    public SouvenirOrderService(SouvenirOrderRepository repository, MoneyUtils moneyUtils) {
        this.repository = repository;
        this.moneyUtils = moneyUtils;
    }

    /**
     * Be cautious as this function returns NULL if there is no Order entity inside provided iterable.
     * Extract prices of each entity nested inside this component. multiply it by number of ordered entities and return.
     *
     * @param souvenirOrderIterable - Iterable containing Product Order entities with nested Product entities.
     * @return NULL if no order entities were provided, MonetaryAmount with sum of all the ordered entities
     * inside provided iterable with matching currency unit otherwise.
     */
    public MonetaryAmount countTotalPrice(Iterable<SouvenirOrder> souvenirOrderIterable) {
        log.traceEntry();
        MonetaryAmount totalPrice = null;

        if (souvenirOrderIterable == null) {
            return Money.zero(moneyUtils.getApplicationCurrencyUnit());
        }

        for (SouvenirOrder order : souvenirOrderIterable) {
            if (order != null) {

                /* Check if return value was already initialized */
                if (totalPrice == null) {
                    totalPrice = Money.zero(order.getSouvenir().getPrice().getCurrency());
                }

                /* Check if Currency Unit of current Order Entity matches Currency Unit of the return object */
                if (totalPrice.getCurrency().getNumericCode() !=
                        order.getSouvenir().getPrice().getCurrency().getNumericCode()) {
                    log.throwing(Level.ERROR,
                            new InvalidOperationException((ProductOrderMessage.ERROR_MATCHING_CURRENCY_UNITS)));
                    throw new InvalidOperationException(ProductOrderMessage.ERROR_MATCHING_CURRENCY_UNITS);
                }

                /* Sum up currently extracted Product Order */
                totalPrice = totalPrice.add(order.getSouvenir().getPrice().multiply(order.getItemCount()));
            }
        }

        log.traceExit();
        return totalPrice;
    }
}
