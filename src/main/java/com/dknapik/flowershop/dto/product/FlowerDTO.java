package com.dknapik.flowershop.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowerDTO implements ProductDTO {
    private UUID id;
    @NotBlank
    private @Valid String name;
    @NotNull
    private @Valid MonetaryAmount price;
    @NotNull
    private @Valid String description;
    @Min(1)
    private @Valid int height;

    /**
     * Retrieves original class of the products
     *
     * @return Class instance of class implementing this interface
     */
    @JsonIgnore
    public Class<?> getProductDTOClass() {
        return this.getClass();
    }

    /**
     * Compare given product original class with this products class.
     *
     * @param classToCompare - Object to compare to
     * @return - true if both classes match.
     */
    @JsonIgnore
    public boolean compareClass(Class<?> classToCompare) {
        return classToCompare.equals(this.getClass());
    }

    /**
     * Cast class implementing this interface to ProductDTO interface
     *
     * @return Class
     */
    @JsonIgnore
    public ProductDTO getProductDTO() {
        return this;
    }
}
