package com.dknapik.flowershop.model.product;

public interface Product {

    /**
     * Retrieves original class of the products
     *
     * @return Class instance of class implementing this interface
     */
    public Class<?> getProductClass();

    /**
     * Compare given product original class with this products class.
     *
     * @param product - Object to compare to
     * @return - true if both classes match.
     */
    public boolean compareClass(Product product);

    /**
     * Cast class implementing this interface to Product interface
     *
     * @return Class
     */
    public Product getProduct();

}
