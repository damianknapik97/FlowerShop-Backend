package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.product.ProductDto;
import com.dknapik.flowershop.dto.product.SouvenirDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SouvenirOrderDto implements ProductOrderDto {
    private UUID id;
    private int itemCount;
    @NotNull
    private SouvenirDto souvenirDto;

    /**
     * Retrieves original class of the product order
     *
     * @return Class instance of class implementing this interface
     */
    @JsonIgnore
    public Class<?> getProductOrderDtoClass() {
        return this.getClass();
    }

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - class to compare to
     * @return - true if both classes match.
     */
    @JsonIgnore
    public boolean compareClass(Class<?> classToCompare) {
        return classToCompare.equals(this.getClass());
    }

    /**
     * Cast class implementing this interface to ProductOrderDto interface
     *
     * @return Class
     */
    @JsonIgnore
    public ProductOrderDto getProductOrderDto() {
        return this;
    }

    /**
     * Returns product associated with class implementing this interface
     *
     * @return - product associated with class implementing this interface
     */
    @JsonIgnore
    public ProductDto getProductDto() {
        return souvenirDto;
    }
}
