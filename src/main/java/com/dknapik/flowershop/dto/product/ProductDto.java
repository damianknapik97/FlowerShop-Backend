package com.dknapik.flowershop.dto.product;

public interface ProductDto {

    /**
     * Retrieves original class of the products
     *
     * @return Class instance of class implementing this interface
     */
    public Class<?> getProductDtoClass();

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - Object to compare to
     * @return - true if both classes match.
     */
    public boolean compareClass(Class<?> classToCompare);

    /**
     * Cast class implementing this interface to ProductDto interface
     *
     * @return Class
     */
    public ProductDto getProductDto();
}
