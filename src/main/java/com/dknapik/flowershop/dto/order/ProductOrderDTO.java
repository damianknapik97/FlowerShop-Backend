package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.DTO;
import com.dknapik.flowershop.dto.product.ProductDTO;

public interface ProductOrderDTO extends DTO {

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

    /**
     * Checks if provided ProductDTO is able to be casted to Product class inside this Order DTO and casts it.
     *
     * @param productDTO - Product DTO to set inside this Order DTO class
     */
    public void setProductDTO(ProductDTO productDTO);
}
