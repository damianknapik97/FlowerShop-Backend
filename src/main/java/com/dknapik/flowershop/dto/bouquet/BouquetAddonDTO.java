package com.dknapik.flowershop.dto.bouquet;

import com.dknapik.flowershop.dto.order.ProductOrderDTO;
import com.dknapik.flowershop.dto.product.AddonDTO;
import com.dknapik.flowershop.dto.product.FlowerDTO;
import com.dknapik.flowershop.dto.product.ProductDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BouquetAddonDTO implements ProductOrderDTO {
    private UUID id;
    @NotNull
    private @Valid AddonDTO addonDTO;

    /**
     * Retrieves original class of the product order
     *
     * @return Class instance of class implementing this interface
     */
    @JsonIgnore
    @Override
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
    @Override
    public boolean compareClass(Class<?> classToCompare) {
        return classToCompare.equals(this.getClass());
    }

    /**
     * Cast class implementing this interface to ProductOrderDTO interface
     *
     * @return Class
     */
    @JsonIgnore
    @Override
    public ProductOrderDTO getProductOrderDTO() {
        return this;
    }

    /**
     * Returns product associated with class implementing this interface
     *
     * @return - product associated with class implementing this interface
     */
    @JsonIgnore
    @Override
    public ProductDTO getProductDTO() {
        return addonDTO;
    }

    /**
     * Checks if provided ProductDTO is able to be casted to Product class inside this Order DTO and casts it.
     *
     * @param productDTO - Product DTO to set inside this Order DTO class
     */
    @JsonIgnore
    @Override
    public void setProductDTO(ProductDTO productDTO) {
        ProductDTO product = Objects.requireNonNull(productDTO);
        if (product.compareClass(AddonDTO.class)) {
            addonDTO = (AddonDTO) product;
        } else {
            throw new IllegalArgumentException("Provided product dto class - " +
                    product.getProductDTOClass().toString() +
                    " - doesn't match the one inside product order dto -" +
                    AddonDTO.class.toString());
        }
    }
}
