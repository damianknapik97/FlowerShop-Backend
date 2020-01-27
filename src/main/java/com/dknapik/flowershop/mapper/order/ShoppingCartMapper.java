package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.order.ShoppingCartDTO;
import com.dknapik.flowershop.mapper.product.ProductMapper;
import com.dknapik.flowershop.model.order.ShoppingCart;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@ToString
public class ShoppingCartMapper {
    private final ModelMapper mapper;
    private final ProductMapper productMapper;
    private final ProductOrderMapper productOrderMapper;


    @Autowired
    public ShoppingCartMapper(ModelMapper mapper, ProductMapper productMapper, ProductOrderMapper productOrderMapper) {
        this.mapper = mapper;
        this.productMapper = productMapper;
        this.productOrderMapper = productOrderMapper;
    }

    /**
     * Maps provided object to entity.
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    public ShoppingCartDTO convertToDto(ShoppingCart entity) {
        return mapper.map(entity, ShoppingCartDTO.class);
    }

    /**
     * Manually convert Souvenir Dto to Entity because of MonetaryAmount attribute.
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    public ShoppingCart convertToEntity(@Valid ShoppingCartDTO dto) {
        return mapper.map(dto, ShoppingCart.class);
    }
}
