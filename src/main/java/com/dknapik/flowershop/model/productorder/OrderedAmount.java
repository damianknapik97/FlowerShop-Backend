package com.dknapik.flowershop.model.productorder;

public interface OrderedAmount {

    /**
     * Returns amount of products that are ordered inside entity implementing this interface.
     *
     * @return integer representing number of products.
     */
    public int getOrderedAmount();
}
