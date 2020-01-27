package com.dknapik.flowershop.dto.bouquet;

import com.dknapik.flowershop.dto.order.ProductOrderDto;
import com.dknapik.flowershop.dto.product.AddonDto;
import com.dknapik.flowershop.dto.product.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BouquetAddonDto implements ProductOrderDto {
    private UUID id;
    @NotNull
    private @Valid AddonDto addonDTO;

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
        return addon;
    }
}
