package com.dknapik.flowershop.mapper.product;

import com.dknapik.flowershop.dto.product.ProductDTO;
import com.dknapik.flowershop.model.product.Product;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ToString
@Log4j2
public class ProductMapper {
    private final ModelMapper modelMapper;


    @Autowired
    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Convert Product Entity to Product Dto using Model Mapper
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    public <T extends ProductDTO> T convertToDto(Product entity, Class<T> type) {
        log.trace(() -> "Mapping " + entity.getProductClass().getSimpleName() + " to " + type.getSimpleName());
        return type.cast(modelMapper.map(entity, type));
    }

    /**
     * Convert Product Dto to Product Entity using Model Mapper
     *
     * @param productDto - dto to map to entity
     * @return - mapped entity
     */
    public <T extends Product> T convertToEntity(ProductDTO productDto, Class<T> type) {
        log.trace(() -> "Mapping " + productDto.getProductDTOClass().getSimpleName() + " to " + type.getSimpleName());
        return type.cast(modelMapper.map(productDto, type));
    }
}
