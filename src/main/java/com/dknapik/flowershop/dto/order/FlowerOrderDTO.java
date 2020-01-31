package com.dknapik.flowershop.dto.order;

import com.dknapik.flowershop.dto.product.FlowerDTO;
import com.dknapik.flowershop.dto.product.ProductDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowerOrderDTO implements ProductOrderDTO {
    private UUID id;
    private int itemCount;
    @NotNull
    private @Valid FlowerDTO flowerDTO;

    /**
     * Retrieves original class of the product order
     *
     * @return Class instance of class implementing this interface
     */
    @JsonIgnore
    public Class<?> getProductOrderDTOClass() {
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
     * Cast class implementing this interface to ProductOrderDTO interface
     *
     * @return Class
     */
    @JsonIgnore
    public ProductOrderDTO getProductOrderDTO() {
        return this;
    }

    /**
     * Returns product associated with class implementing this interface
     *
     * @return - product associated with class implementing this interface
     */
    @JsonIgnore
    public ProductDTO getProductDTO() {
        return flowerDTO;
    }
}
