package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.order.OrderDTO;
import com.dknapik.flowershop.model.order.Order;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Log4j2
@ToString
public final class OrderMapper {
    private final ModelMapper mapper;
    private final DeliveryAddressMapper deliveryAddressMapper;
    private final PaymentMapper paymentMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Autowired
    public OrderMapper(ModelMapper mapper,
                       DeliveryAddressMapper deliveryAddressMapper,
                       PaymentMapper paymentMapper,
                       ShoppingCartMapper shoppingCartMapper) {
        this.mapper = mapper;
        this.deliveryAddressMapper = deliveryAddressMapper;
        this.paymentMapper = paymentMapper;
        this.shoppingCartMapper = shoppingCartMapper;
    }

    /**
     * Map Order Entity to Order DTO using Jackson Model Mapper and other mappers related to embedded entities.
     *
     * @param order - entity
     * @return - dto
     */
    public OrderDTO mapToDTO(Order order) {
        log.traceEntry(() -> "Mapping " + order.getClass().getSimpleName() + " to " + OrderDTO.class.getSimpleName());
        OrderDTO mappedDTO = mapper.map(order, OrderDTO.class);

        /* Manually map embedded entities because Jackson Model Mapper doesn't handle them well  */
        if (order.getDeliveryAddress() != null) {
            mappedDTO.setDeliveryAddressDTO(deliveryAddressMapper.mapToDTO(order.getDeliveryAddress()));
        }
        if (order.getPayment() != null) {
            mappedDTO.setPaymentDTO(paymentMapper.mapToDTO(order.getPayment()));
        }

        if (order.getShoppingCart() != null) {
            mappedDTO.setShoppingCartDTO(shoppingCartMapper.convertToDTO(order.getShoppingCart()));
        }

        log.traceExit();
        return mappedDTO;
    }

    /**
     * Map Order DTO to Order Entity using Jackson Model Mapper and other mappers related to embedded DTOs
     *
     * @param orderDTO - dto
     * @return - entity
     */
    public Order mapToEntity(OrderDTO orderDTO) {
        log.traceEntry(() -> "Mapping " + orderDTO.getClass().getSimpleName() + " to " + Order.class.getSimpleName());
        Order mappedEntity = mapper.map(orderDTO, Order.class);

        /* Manually map embedded DTO's because Jackson Model Mapper doesn't handle them well */
        if (orderDTO.getDeliveryAddressDTO() != null) {
            mappedEntity.setDeliveryAddress(deliveryAddressMapper.mapToEntity(orderDTO.getDeliveryAddressDTO()));
        }
        if (orderDTO.getPaymentDTO() != null) {
            mappedEntity.setPayment(paymentMapper.mapToEntity(orderDTO.getPaymentDTO()));
        }

        if (orderDTO.getShoppingCartDTO() != null) {
            mappedEntity.setShoppingCart(shoppingCartMapper.convertToEntity(orderDTO.getShoppingCartDTO()));
        }

        log.traceExit();
        return mappedEntity;
    }

    /**
     * Maps provided Page Implementation content with Orders, to Order DTO Rest Response Page.
     *
     * @param orderPage
     * @return
     */
    public RestResponsePage<OrderDTO> mapPageToDTO(PageImpl<Order> orderPage) {
        log.traceEntry();

        List<Order> currentContent = orderPage.getContent();
        List<OrderDTO> mappedContent = new LinkedList<>();

        for (Order order : currentContent) {
            mappedContent.add(mapToDTO(order));
        }

        log.traceExit();
        return new RestResponsePage<>(mappedContent, orderPage.getPageable(), orderPage.getTotalElements());
    }

}
