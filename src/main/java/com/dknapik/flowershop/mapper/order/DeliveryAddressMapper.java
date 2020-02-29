package com.dknapik.flowershop.mapper.order;

import com.dknapik.flowershop.dto.order.DeliveryAddressDTO;
import com.dknapik.flowershop.mapper.Mapper;
import com.dknapik.flowershop.model.order.DeliveryAddress;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@ToString
@Service
public final class DeliveryAddressMapper implements Mapper<DeliveryAddress, DeliveryAddressDTO> {
    private final ModelMapper mapper;

    @Autowired
    public DeliveryAddressMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Map provided DeliveryAddress entity to DeliveryAddress DTO using Jackson Model Mapper.
     *
     * @param model - entity
     * @return - dto
     */
    @Override
    public DeliveryAddressDTO mapToDTO(DeliveryAddress model) {
        log.traceEntry();
        return mapper.map(model, DeliveryAddressDTO.class);
    }

    /**
     * Map provided DeliveryAddress DTO to DeliveryAddress Entity using Jackson Model Mapper.
     *
     * @param dto - dto
     * @return - entity
     */
    @Override
    public DeliveryAddress mapToEntity(DeliveryAddressDTO dto) {
        log.traceEntry();
        return mapper.map(dto, DeliveryAddress.class);
    }
}
