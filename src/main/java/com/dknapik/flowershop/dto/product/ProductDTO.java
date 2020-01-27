package com.dknapik.flowershop.dto.product;

public interface ProductDTO {

    /**
     * Retrieves original class of the products
     *
     * @return Class instance of class implementing this interface
     */
    public Class<?> getProductDTOClass();

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - Object to compare to
     * @return - true if both classes match.
     */
    public boolean compareClass(Class<?> classToCompare);

    /**
     * Cast class implementing this interface to ProductDTO interface
     *
     * @return Class
     */
    public ProductDTO getProductDTO();
}
