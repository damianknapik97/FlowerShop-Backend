package com.dknapik.flowershop.mapper;

import com.dknapik.flowershop.dto.bouquet.BouquetAddonDTO;
import com.dknapik.flowershop.dto.bouquet.BouquetDTO;
import com.dknapik.flowershop.dto.bouquet.BouquetFlowerDTO;
import com.dknapik.flowershop.model.bouquet.Bouquet;
import com.dknapik.flowershop.model.bouquet.BouquetAddon;
import com.dknapik.flowershop.model.bouquet.BouquetFlower;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ToString
@Log4j2
public final class BouquetMapper implements Mapper<Bouquet, BouquetDTO> {
    private final ModelMapper modelMapper;
    private final ProductOrderMapper productOrderMapper;

    @Autowired
    public BouquetMapper(ModelMapper modelMapper, ProductOrderMapper productOrderMapper) {
        this.modelMapper = modelMapper;
        this.productOrderMapper = productOrderMapper;
    }

    /**
     * Map provided Model object to Data Transfer Object
     *
     * @param model - Model object to map
     * @return DTO object created from provided Model
     */
    @Override
    public BouquetDTO mapToDTO(Bouquet model) {
        log.traceEntry();

        BouquetDTO returnValue = modelMapper.map(model, BouquetDTO.class);
        returnValue = mapCustomNestedTypesToDTO(model, returnValue);

        log.traceExit();
        return returnValue;
    }

    /**
     * Map provided Data Transfer Object to Model object
     *
     * @param dto - Data Transfer Object
     * @return Model object created from provided DTO
     */
    @Override
    public Bouquet mapToEntity(BouquetDTO dto) {
        log.traceEntry();

        Bouquet returnValue = modelMapper.map(dto, Bouquet.class);
        returnValue = mapCustomNestedTypesToEntity(dto, returnValue);

        log.traceExit();
        return returnValue;
    }

    /**
     * Converts variables that have custom defined type and are not supported by Model Mapper to appropriate DTOs
     *
     * @param source - Source bouquet entity
     * @param target - Target bouquet DTO
     * @return Bouquet DTO provided in argument with mapped custom variable types
     */
    private BouquetDTO mapCustomNestedTypesToDTO(@NonNull Bouquet source, @NonNull BouquetDTO target) {
        log.traceEntry();

        target.setBouquetFlowerList(productOrderMapper.convertProductOrderListToDTO(
                source.getBouquetFlowerList(), BouquetFlowerDTO.class));
        target.setBouquetAddonList(productOrderMapper.convertProductOrderListToDTO(
                source.getBouquetAddonList(), BouquetAddonDTO.class));

        log.traceExit();
        return target;
    }

    /**
     * Converts variables that have custom defined type and are not supported by Model Mapper to appropriate entities
     *
     * @param source - Source bouquet dto
     * @param target - Target bouquet entity
     * @return Bouquet entity provided in argument with mapped custom variable types
     */
    private Bouquet mapCustomNestedTypesToEntity(@NonNull BouquetDTO source, @NonNull Bouquet target) {
        log.traceEntry();

        target.setBouquetFlowerList(productOrderMapper.convertProductOrderDTOListToEntity(
                source.getBouquetFlowerList(), BouquetFlower.class));
        target.setBouquetAddonList(productOrderMapper.convertProductOrderDTOListToEntity(
                source.getBouquetAddonList(), BouquetAddon.class));

        log.traceExit();
        return target;
    }
}
