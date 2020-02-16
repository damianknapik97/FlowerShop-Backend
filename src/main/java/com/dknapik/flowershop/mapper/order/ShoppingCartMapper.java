package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.order.FlowerOrderDTO;
import com.dknapik.flowershop.dto.order.OccasionalArticleOrderDTO;
import com.dknapik.flowershop.dto.order.ShoppingCartDTO;
import com.dknapik.flowershop.dto.order.SouvenirOrderDTO;
import com.dknapik.flowershop.mapper.product.ProductMapper;
import com.dknapik.flowershop.model.order.FlowerOrder;
import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.order.SouvenirOrder;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/* TODO: Add bouquet handling */
@Service
@ToString
@Log4j2
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

    public ShoppingCartDTO convertToDTO(ShoppingCart entity) {
        log.trace(() -> "Mapping " + ShoppingCart.class.toString() + " to " + ShoppingCartDTO.class.toString());
        ShoppingCartDTO returnDTO = mapper.map(entity, ShoppingCartDTO.class);

        /* Map products */
        /* TODO: Is there a better way ??? */
        if (entity.getOccasionalArticleOrderList() != null) {
            returnDTO.setOccasionalArticleOrderDTOs(
                    convertOccasionalArticleOrdersToDTO(entity.getOccasionalArticleOrderList()));
        }

        if (entity.getFlowerOrderList() != null) {
            returnDTO.setFlowerOrderDTOs(convertFlowerOrdersToDTO(entity.getFlowerOrderList()));
        }

        if (entity.getSouvenirOrderList() != null) {
            returnDTO.setSouvenirOrderDTOs(convertSouvenirOrdersToDTO(entity.getSouvenirOrderList()));
        }

        return returnDTO;
    }

    /**
     * TODO: Needs refactoring if there will be ever need to use this function.
     *
     * Manually convert Souvenir Dto to Entity because of MonetaryAmount attribute.
     *
     * @param dto - dto to map to entity
     * @return - mapped entity

    public ShoppingCart convertToEntity(@Valid ShoppingCartDTO dto) {
    return mapper.map(dto, ShoppingCart.class);
    }
     */


    /**
     * Converts provided iterable with order entities into list with dto orders.
     *
     * @param occasionalArticleOrders - iterable with entities that can be casted to DTOs
     * @return null if provided argument is null, mapped list with DTOs otherwise
     */
    private List<OccasionalArticleOrderDTO> convertOccasionalArticleOrdersToDTO(
            Iterable<OccasionalArticleOrder> occasionalArticleOrders) {
        log.debug(() ->
                "Casting " + OccasionalArticleOrder.class.toString() + " iterables to List with its DTO equivalents");
        List<OccasionalArticleOrderDTO> convertedDTOs = null;

        /* Check if there are any Order entities to convert to DTO and convert them. */
        if (occasionalArticleOrders != null) {
            convertedDTOs = new LinkedList<>();
            for (OccasionalArticleOrder occasionalArticleOrder : occasionalArticleOrders) {
                convertedDTOs.add(
                        productOrderMapper.convertToDto(occasionalArticleOrder, OccasionalArticleOrderDTO.class));
            }
        }
        return convertedDTOs;
    }

    /**
     * Converts provided iterable with order entities into list with dto orders.
     *
     * @param flowerOrders - iterable with entities that can be casted to DTOs
     * @return null if provided argument is null, mapped list with DTOs otherwise
     */
    private List<FlowerOrderDTO> convertFlowerOrdersToDTO(
            Iterable<FlowerOrder> flowerOrders) {
        log.debug(() -> "Casting " + FlowerOrder.class.toString() + " iterables to List with its DTO equivalents");
        List<FlowerOrderDTO> convertedDTOs = null;

        /* Check if there are any Order entities to convert to DTO and convert them. */
        if (flowerOrders != null) {
            convertedDTOs = new LinkedList<>();
            for (FlowerOrder flowerOrder : flowerOrders) {
                convertedDTOs.add(productOrderMapper.convertToDto(flowerOrder, FlowerOrderDTO.class));
            }
        }
        return convertedDTOs;
    }

    /**
     * Converts provided iterable with order entities into list with dto orders.
     *
     * @param souvenirOrders - iterable with entities that can be casted to DTOs
     * @return null if provided argument is null, mapped list with DTOs otherwise
     */
    private List<SouvenirOrderDTO> convertSouvenirOrdersToDTO(
            Iterable<SouvenirOrder> souvenirOrders) {
        log.debug(() -> "Casting " + SouvenirOrder.class.toString() + " iterables to List with its DTO equivalents");
        List<SouvenirOrderDTO> convertedDTOs = null;

        /* Check if there are any Order entities to convert to DTO and convert them. */
        if (souvenirOrders != null) {
            convertedDTOs = new LinkedList<>();
            for (SouvenirOrder souvenirOrder : souvenirOrders) {
                convertedDTOs.add(productOrderMapper.convertToDto(souvenirOrder, SouvenirOrderDTO.class));
            }
        }
        return convertedDTOs;
    }
}
