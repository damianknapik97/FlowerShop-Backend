package com.dknapik.flowershop.services.productorder;

import com.dknapik.flowershop.constants.ProductOrderMessage;
import com.dknapik.flowershop.database.order.OccasionalArticleOrderRepository;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.model.productorder.OccasionalArticleOrder;
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
public final class OccasionalArticleOrderService {
    private final OccasionalArticleOrderRepository repository;
    private final MoneyUtils moneyUtils;

    @Autowired
    public OccasionalArticleOrderService(OccasionalArticleOrderRepository repository, MoneyUtils moneyUtils) {
        this.repository = repository;
        this.moneyUtils = moneyUtils;
    }

    /**
     * Count total price of Occasional Article Orders nested inside provided iterable. If the provided iterable is
     * null or empty, number zero with application currently defined currency code is returned.
     *
     * @param occasionalArticleOrderIterable - Iterable containing Product Order entities with nested Product entities.
     * @return Monetary Amount containing zero and application currency if there is no products to count price from,
     * or total sum of all products inside provided iterable.
     */
    public MonetaryAmount countTotalPrice(Iterable<OccasionalArticleOrder> occasionalArticleOrderIterable) {
        log.traceEntry();
        MonetaryAmount totalPrice = null;

        if (occasionalArticleOrderIterable == null) {
            return Money.zero(moneyUtils.getApplicationCurrencyUnit());
        }

        for (OccasionalArticleOrder order : occasionalArticleOrderIterable) {
            if (order != null) {

                /* Check if return value was already initialized */
                if (totalPrice == null) {
                    totalPrice = Money.zero(order.getOccasionalArticle().getPrice().getCurrency());
                }

                /* Check if Currency Unit of current Order Entity matches Currency Unit of the return object */
                if (totalPrice.getCurrency().getNumericCode() !=
                        order.getOccasionalArticle().getPrice().getCurrency().getNumericCode()) {
                    log.throwing(Level.ERROR,
                            new InvalidOperationException((ProductOrderMessage.ERROR_MATCHING_CURRENCY_UNITS)));
                    throw new InvalidOperationException(ProductOrderMessage.ERROR_MATCHING_CURRENCY_UNITS);
                }

                /* Sum up currently extracted Product Order */
                totalPrice = totalPrice.add(order.getOccasionalArticle().getPrice().multiply(order.getItemCount()));
            }
        }

        log.traceExit();
        return totalPrice;
    }
}
