package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.product.ProductDto;

public interface ProductOrderDto {

    /**
     * Retrieves original class of the product order
     *
     * @return Class instance of class implementing this interface
     */
    public Class<?> getProductOrderDtoClass();

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - class to compare to
     * @return - true if both classes match.
     */
    public boolean compareClass(Class<?> classToCompare);

    /**
     * Cast class implementing this interface to ProductOrderDto interface
     *
     * @return Class
     */
    public ProductOrderDto getProductOrderDto();

    /**
     * Returns product associated with class implementing this interface
     *
     * @return - product associated with class implementing this interface
     */
    public ProductDto getProductDto();
}
