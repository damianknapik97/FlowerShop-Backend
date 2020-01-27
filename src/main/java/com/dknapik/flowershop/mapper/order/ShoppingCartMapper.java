package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.order.ShoppingCartDto;
import com.dknapik.flowershop.model.order.ShoppingCart;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ToString
public class ShoppingCartMapper {
    private final ModelMapper mapper;


    @Autowired
    public ShoppingCartMapper(ModelMapper mapper) {
        this.mapper = mapper;

    }

    /**
     * Maps provided object to entity.
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    public ShoppingCartDto convertToDto(ShoppingCart entity) {
        return mapper.map(entity, ShoppingCartDto.class);
    }


    /**
     * Manually convert Souvenir Dto to Entity because of MonetaryAmount attribute.
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    public ShoppingCart convertToEntity(ShoppingCartDto dto) {
        return mapper.map(dto, ShoppingCart.class);
    }
}
