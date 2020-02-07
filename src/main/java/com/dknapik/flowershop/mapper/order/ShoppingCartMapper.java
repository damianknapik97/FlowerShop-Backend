package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.order.*;
import com.dknapik.flowershop.dto.product.ProductDTO;
import com.dknapik.flowershop.mapper.product.ProductMapper;
import com.dknapik.flowershop.model.order.*;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

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
        ShoppingCartDTO returnDTO =
                new ShoppingCartDTO(entity.getId(), entity.getName(), null, null, null, null);

        List<List<? extends ProductOrderDTO>> allProductOrdersDTO = new LinkedList<>();
        List<List<? extends ProductOrder>> allProductOrders = entity.getAllProductOrders();
        for (List<? extends ProductOrder> productOrdersList : allProductOrders) {
            List<ProductOrderDTO> productOrderDTOs = new LinkedList<>();

            for (ProductOrder productOrder : productOrdersList) {
                productOrderDTOs.add(mapProductOrderDTO(productOrder));
            }

            allProductOrdersDTO.add(productOrderDTOs);
        }
        returnDTO.setProductOrderDTOs(allProductOrdersDTO);
        return returnDTO;
    }

    public ProductOrderDTO mapProductOrderDTO(ProductOrder productOrder) {
        ProductOrderDTO productOrderDTO = null;
        if (productOrder.compareClass(FlowerOrder.class)) {
            productOrderDTO = productOrderMapper.convertToDto(productOrder, FlowerOrderDTO.class);
        } else if (productOrder.compareClass(OccasionalArticleOrder.class)) {
            productOrderDTO = productOrderMapper.convertToDto(productOrder, OccasionalArticleOrderDTO.class);
        } else if (productOrder.compareClass(SouvenirOrder.class)) {
            productOrderDTO = productOrderMapper.convertToDto(productOrder, SouvenirOrderDTO.class);
        }
        return productOrderDTO;
    }

    public ProductDTO mapProductDTO() {
        return null;
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
