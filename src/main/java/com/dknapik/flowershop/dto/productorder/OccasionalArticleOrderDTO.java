package com.dknapik.flowershop.dto.productorder;

import com.dknapik.flowershop.dto.product.OccasionalArticleDTO;
import com.dknapik.flowershop.dto.product.ProductDTO;
import com.dknapik.flowershop.dto.productorder.ProductOrderDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OccasionalArticleOrderDTO implements ProductOrderDTO {
    private UUID id;
    @Min(1)
    private @Valid int itemCount;
    @NotNull
    private @Valid OccasionalArticleDTO occasionalArticleDTO;

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
        return occasionalArticleDTO;
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
        if (product.compareClass(OccasionalArticleDTO.class)) {
            occasionalArticleDTO = (OccasionalArticleDTO) product;
        } else {
            throw new IllegalArgumentException("Provided product dto class - " +
                    product.getProductDTOClass().toString() +
                    " - doesn't match the one inside product order dto -" +
                    OccasionalArticleDTO.class.toString());
        }
    }
}
