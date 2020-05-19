package com.dknapik.flowershop.services.productorder;

import com.dknapik.flowershop.constants.ProductOrderMessage;
import com.dknapik.flowershop.database.order.FlowerOrderRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.model.productorder.FlowerOrder;
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
public final class FlowerOrderService {
    private final FlowerOrderRepository flowerOrderRepository;
    private final MoneyUtils moneyUtils;

    @Autowired
    public FlowerOrderService(FlowerOrderRepository flowerOrderRepository, MoneyUtils moneyUtils) {
        this.flowerOrderRepository = flowerOrderRepository;
        this.moneyUtils = moneyUtils;
    }


    /**
     * Count total price of Flower Orders nested inside provided iterable. If the provided iterable is
     * null or empty, number zero with application currently defined currency code is returned.
     *
     * @param flowerOrderIterable - Iterable containing Product Order entities with nested Product entities.
     * @return Monetary Amount containing zero and application currency if there is no products to count price from,
     * or total sum of all products inside provided iterable.
     */
    public MonetaryAmount countTotalPrice(Iterable<FlowerOrder> flowerOrderIterable) {
        log.traceEntry();
        MonetaryAmount totalPrice = null;

        if (flowerOrderIterable == null) {
            return Money.zero(moneyUtils.getApplicationCurrencyUnit());
        }

        for (FlowerOrder order : flowerOrderIterable) {
            if (order != null) {

                /* Check if return value was already initialized */
                if (totalPrice == null) {
                    totalPrice = Money.zero(order.getFlower().getPrice().getCurrency());
                }

                /* Check if Currency Unit of current Order Entity matches Currency Unit of the return object */
                if (totalPrice.getCurrency().getNumericCode() !=
                        order.getFlower().getPrice().getCurrency().getNumericCode()) {
                    log.throwing(Level.ERROR,
                            new InvalidOperationException((ProductOrderMessage.ERROR_MATCHING_CURRENCY_UNITS)));
                    throw new InvalidOperationException(ProductOrderMessage.ERROR_MATCHING_CURRENCY_UNITS);
                }

                /* Sum up currently extracted Product Order */
                totalPrice = totalPrice.add(order.getFlower().getPrice().multiply(order.getItemCount()));
            }
        }

        log.traceExit();
        return totalPrice;
    }
}
