package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.order.ProductOrderDto;
import com.dknapik.flowershop.model.order.ProductOrder;
import com.dknapik.flowershop.model.product.Product;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ToString
@Log4j2
public class ProductOrderMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ProductOrderMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Convert Product Order Entity to Product Order Dto using Model Mapper
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    public <T extends ProductOrderDto> T convertToDto(ProductOrder entity, Class<T> type) {
        log.trace(() -> "Mapping " + entity.getProductOrderClass().getSimpleName() + " to " + type.getSimpleName());
        return type.cast(modelMapper.map(entity, type));
    }

    /**
     * Convert Product Order Dto to Product Order Entity using Model Mapper
     *
     * @param productOrderDto - dto to map to entity
     * @return - mapped entity
     */
    public <T extends Product> T convertToEntity(ProductOrderDto productOrderDto, Class<T> type) {
        log.trace(() ->
                "Mapping " + productOrderDto.getProductOrderDtoClass().getSimpleName() + " to " + type.getSimpleName());
        return type.cast(modelMapper.map(productOrderDto, type));
    }

}
