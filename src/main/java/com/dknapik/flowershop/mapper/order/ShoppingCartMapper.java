package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.productorder.FlowerOrderDTO;
import com.dknapik.flowershop.dto.productorder.OccasionalArticleOrderDTO;
import com.dknapik.flowershop.dto.order.ShoppingCartDTO;
import com.dknapik.flowershop.dto.productorder.SouvenirOrderDTO;
import com.dknapik.flowershop.mapper.product.ProductMapper;
import com.dknapik.flowershop.model.productorder.FlowerOrder;
import com.dknapik.flowershop.model.productorder.OccasionalArticleOrder;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.productorder.SouvenirOrder;
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
public final class ShoppingCartMapper {
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
        log.traceEntry(() -> "Mapping " + ShoppingCart.class.getSimpleName() +
                " to " + ShoppingCartDTO.class.getSimpleName());
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

        log.traceExit();
        return returnDTO;
    }

    /**
     * Manually convert Souvenir Dto to Entity because of MonetaryAmount attribute.
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    public ShoppingCart convertToEntity(ShoppingCartDTO dto) {
        log.traceEntry(() -> "Mapping " + ShoppingCartDTO.class.getSimpleName() +
                " to " + ShoppingCartDTO.class.getSimpleName());

        ShoppingCart returnDTO = mapper.map(dto, ShoppingCart.class);
        /* Map products */
        if (dto.getFlowerOrderDTOs() != null) {
            returnDTO.setFlowerOrderList(convertFlowerOrdersToEntity(dto.getFlowerOrderDTOs()));
        }
        if (dto.getOccasionalArticleOrderDTOs() != null) {
            returnDTO.setOccasionalArticleOrderList(convertOccasionalArticleOrdersToEntity(
                    dto.getOccasionalArticleOrderDTOs()));
        }
        if (dto.getSouvenirOrderDTOs() != null) {
            returnDTO.setSouvenirOrderList(convertSouvenirOrdersToEntity(dto.getSouvenirOrderDTOs()));
        }

        log.traceExit();
        return returnDTO;
    }

    /**
     * Converts provided iterable with order entities into list with dto orders.
     *
     * @param occasionalArticleOrders - iterable with entities that can be casted to DTOs
     * @return null if provided argument is null, mapped list with DTOs otherwise
     */
    private List<OccasionalArticleOrderDTO> convertOccasionalArticleOrdersToDTO(
            Iterable<OccasionalArticleOrder> occasionalArticleOrders) {
        log.debug(() -> "Casting " + OccasionalArticleOrder.class.getSimpleName() +
                " iterables to List with its DTO equivalents");

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
        log.debug(() -> "Casting " + FlowerOrder.class.getSimpleName() + " iterables to List with its DTO equivalents");

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
        log.debug(() -> "Casting " + SouvenirOrder.class.getSimpleName() +
                " iterables to List with its DTO equivalents");

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

    /**
     * Convert provided iterable of Order DTOs to List with Order Entities.
     *
     * @param flowerOrderDTOs - iterable with DTOs that can be casted to Entities
     * @return null if provided argument is null, mapped list with Entities otherwise
     */
    private List<FlowerOrder> convertFlowerOrdersToEntity(Iterable<FlowerOrderDTO> flowerOrderDTOs) {
        log.debug(() -> "Casting " + FlowerOrderDTO.class.getSimpleName() +
                " iterables to List with its Entity equivalents");

        List<FlowerOrder> returnList = null;

        /* Check if there are any Order DTOs to convert to Entity and convert them. */
        if (flowerOrderDTOs != null) {
            returnList = new LinkedList<>();
            for (FlowerOrderDTO orderDTO : flowerOrderDTOs) {
                returnList.add(productOrderMapper.convertToEntity(orderDTO, FlowerOrder.class));
            }
        }

        return returnList;
    }

    /**
     * Convert provided iterable of Order DTOs to List with Order Entities.
     *
     * @param occasionalArticleOrderDTOs - iterable with DTOs that can be casted to Entities
     * @return null if provided argument is null, mapped list with Entities otherwise
     */
    private List<OccasionalArticleOrder> convertOccasionalArticleOrdersToEntity(
            Iterable<OccasionalArticleOrderDTO> occasionalArticleOrderDTOs) {
        List<OccasionalArticleOrder> returnList = null;
        log.debug(() -> "Casting " + OccasionalArticleOrderDTO.class.getSimpleName() +
                " iterables to List with its Entity equivalents");

        /* Check if there are any Order DTOs to convert to Entity and convert them. */
        if (occasionalArticleOrderDTOs != null) {
            returnList = new LinkedList<>();
            for (OccasionalArticleOrderDTO orderDTO : occasionalArticleOrderDTOs) {
                returnList.add(productOrderMapper.convertToEntity(orderDTO, OccasionalArticleOrder.class));
            }
        }
        return returnList;
    }

    /**
     * Convert provided iterable of Order DTOs to List with Order Entities.
     *
     * @param souvenirOrderDTOs - iterable with DTOs that can be casted to Entities
     * @return null if provided argument is null, mapped list with Entities otherwise
     */
    private List<SouvenirOrder> convertSouvenirOrdersToEntity(Iterable<SouvenirOrderDTO> souvenirOrderDTOs) {
        log.debug(() -> "Casting " + SouvenirOrderDTO.class.getSimpleName() +
                " iterables to List with its Entity equivalents");

        List<SouvenirOrder> returnList = null;
        /* Check if there are any Order DTOs to convert to Entity and convert them. */
        if (souvenirOrderDTOs != null) {
            returnList = new LinkedList<>();
            for (SouvenirOrderDTO orderDTO : souvenirOrderDTOs) {
                returnList.add(productOrderMapper.convertToEntity(orderDTO, SouvenirOrder.class));
            }
        }
        return returnList;
    }
}
