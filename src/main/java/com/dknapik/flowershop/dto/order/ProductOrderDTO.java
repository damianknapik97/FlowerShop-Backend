package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.product.ProductDTO;

public interface ProductOrderDTO {

    /**
     * Retrieves original class of the product order
     *
     * @return Class instance of class implementing this interface
     */
    public Class<?> getProductOrderDTOClass();

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - class to compare to
     * @return - true if both classes match.
     */
    public boolean compareClass(Class<?> classToCompare);

    /**
     * Cast class implementing this interface to ProductOrderDTO interface
     *
     * @return Class
     */
    public ProductOrderDTO getProductOrderDTO();

    /**
     * Returns product associated with class implementing this interface
     *
     * @return - product associated with class implementing this interface
     */
    public ProductDTO getProductDTO();
}
