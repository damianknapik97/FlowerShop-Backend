package com.dknapik.flowershop.services.productorder;

import com.dknapik.flowershop.constants.ProductOrderMessage;
import com.dknapik.flowershop.exceptions.runtime.InvalidOperationException;
import com.dknapik.flowershop.model.productorder.ProductOrder;
import com.dknapik.flowershop.services.product.ProductService;
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
public final class ProductOrderService {
    private final MoneyUtils moneyUtils;
    private final ProductService productService;

    @Autowired
    public ProductOrderService(MoneyUtils moneyUtils, ProductService productService) {
        this.moneyUtils = moneyUtils;
        this.productService = productService;
    }

    /**
     * Extracts number of entities and product from provided product order and counts its total price. If provided
     * product order is null, then zero with actual application currency unit is returned.
     *
     * @param productOrder - product order to count total price from
     * @return - product unit price multiplied by number of entities inside product order
     */
    public MonetaryAmount countTotalPrice(@Nullable ProductOrder productOrder) {
        if (productOrder == null) {
            return moneyUtils.zeroWithApplicationCurrency();
        }

        return productService.countTotalPrice(productOrder.getOrderedAmount(), productOrder.getProduct());
    }

    /**
     * Counts total sum of all the product orders prices inside provided Iterable. If currency units doesn't match
     * between mentioned entities, an InvalidOperationException is thrown. If product orders is null or it doesn't
     * contain any objects inside, MonetaryAmount containing zero and application currency unit is returned.
     *
     * @param productOrders - iterable with product orders entities
     * @return MonetaryAmount with sum of all the product orders
     */
    public MonetaryAmount countTotalCollectionPrice(@Nullable Iterable<? extends ProductOrder> productOrders) {
        log.traceEntry();

        if (productOrders == null) {
            return moneyUtils.zeroWithApplicationCurrency();
        }

        MonetaryAmount totalPrice = null;
        MonetaryAmount productOrderPrice;

        for (ProductOrder productOrder : productOrders) {
            /* Count total price for product order */
            productOrderPrice = countTotalPrice(productOrder);

            /* Check if total price is zero, if true there is no point in performing calculations */
            if (!productOrderPrice.isZero()) {

                /* Check if return variable was initialized as it needs to have Currency Unit assigned from extracted
                 * product entity instead of the whole application if we would like to introduce multiple
                 * currency unit support in the future.  */
                if (totalPrice == null) {
                    totalPrice = countTotalPrice(productOrder);
                } else {

                    /* Check if currency of freshly calculated product order, matches the currency of the rest.
                     *  If not, we do not want to lose money by creating under priced total price
                     * so we throw exception*/
                    if (!moneyUtils.checkMatchingCurrencies(totalPrice.getCurrency(),
                            productOrderPrice.getCurrency())) {
                        log.throwing(new InvalidOperationException(ProductOrderMessage.ERROR_MATCHING_CURRENCY_UNITS));
                        throw new InvalidOperationException(ProductOrderMessage.ERROR_MATCHING_CURRENCY_UNITS);
                    }

                    /* If everything else is fine, add counted price to total price */
                    totalPrice = totalPrice.add(productOrderPrice);
                }
            }
        }

        /* In order to not to return null, we check if above calculation even initialized return variable.
         *  If not, we initialize it with 0 value and application currently used currency. */
        if (totalPrice == null) {
            totalPrice = moneyUtils.zeroWithApplicationCurrency();
        }

        log.traceExit();
        return totalPrice;
    }
}
