package com.dknapik.flowershop.model.product;

import com.dknapik.flowershop.model.Model;

public interface Product extends Model, UnitPrice {

    /**
     * Retrieves original class of the products
     *
     * @return Class instance of class implementing this interface
     */
    public Class<?> getProductClass();

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - Object to compare to
     * @return - true if both classes match.
     */
    public boolean compareClass(Class<?> classToCompare);

    /**
     * Cast class implementing this interface to Product interface
     *
     * @return Class
     */
    public Product getProduct();

}
