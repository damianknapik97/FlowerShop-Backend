package com.dknapik.flowershop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SouvenirDto implements ProductDto {
    private UUID id;
    @NotBlank
    private String name;
    @NotNull
    private MonetaryAmount price;
    @NotNull
    private String description;

    /**
     * Retrieves original class of the products
     *
     * @return Class instance of class implementing this interface
     */
    public Class<?> getProductDtoClass() {
        return this.getClass();
    }

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - Object to compare to
     * @return - true if both classes match.
     */
    public boolean compareClass(Class<?> classToCompare) {
        return classToCompare.equals(this.getClass());
    }

    /**
     * Cast class implementing this interface to ProductDto interface
     *
     * @return Class
     */
    public ProductDto getProductDto() {
        return this;
    }
}
