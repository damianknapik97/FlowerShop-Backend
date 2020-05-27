package com.dknapik.flowershop.model.product;

import javax.money.MonetaryAmount;

public interface UnitPrice {

    /**
     * Returns product price per single unit.
     *
     * @return MonetaryAmount containing amount and currency of a single product.
     */
    public MonetaryAmount getUnitPrice();
}
