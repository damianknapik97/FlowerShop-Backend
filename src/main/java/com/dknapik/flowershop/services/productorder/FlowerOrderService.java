package com.dknapik.flowershop.services.productorder;

import com.dknapik.flowershop.constants.ProductOrderMessage;
import com.dknapik.flowershop.database.order.FlowerOrderRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.model.productorder.FlowerOrder;
import com.dknapik.flowershop.model.productorder.OccasionalArticleOrder;
import lombok.NonNull;
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

    @Autowired
    public FlowerOrderService(FlowerOrderRepository flowerOrderRepository) {
        this.flowerOrderRepository = flowerOrderRepository;
    }


    /**
     * Be cautious as this function returns NULL if there is no Order entity inside provided iterable.
     * Extract prices of each entity nested inside this component. multiply it by number of ordered entities and return.
     *
     * @param flowerOrderIterable - Iterable containing Product Order entities with nested Product entities.
     * @return NULL if no order entities were provided, MonetaryAmount with sum of all the ordered entities
     * inside provided iterable with matching currency unit otherwise.
     */
    public MonetaryAmount countTotalPrice(@NonNull Iterable<FlowerOrder> flowerOrderIterable) {
        log.traceEntry();
        MonetaryAmount totalPrice = null;

        for (FlowerOrder order: flowerOrderIterable) {
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
