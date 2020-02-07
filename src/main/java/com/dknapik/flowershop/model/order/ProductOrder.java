package com.dknapik.flowershop.model.order;


import com.dknapik.flowershop.model.product.Product;

public interface ProductOrder {

    /**
     * Retrieves original class of the product order
     *
     * @return Class instance of class implementing this interface
     */
    public Class<?> getProductOrderClass();

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - class to compare to
     * @return - true if both classes match.
     */
    public boolean compareClass(Class<?> classToCompare);

    /**
     * Cast class implementing this interface to ProductOrder interface
     *
     * @return Class
     */
    public ProductOrder getProductOrder();

    /**
     * Returns product associated with class implementing this interface
     *
     * @return - product associated with class implementing this interface
     */
    public Product getProduct();
}