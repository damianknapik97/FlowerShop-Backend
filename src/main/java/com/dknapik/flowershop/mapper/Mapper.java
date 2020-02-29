package com.dknapik.flowershop.mapper;

import com.dknapik.flowershop.dto.DTO;
import com.dknapik.flowershop.model.Model;

public interface Mapper<T extends Model, Y extends DTO> {

    /**
     * Map provided Model object to Data Transfer Object
     *
     * @param model - Model object to map
     * @return DTO object created from provided Model
     */
    public Y mapToDTO(T model);

    /**
     * Map provided Data Transfer Object to Model object
     *
     * @param dto - Data Transfer Object
     * @return Model object created from provided DTO
     */
    public T mapToEntity(Y dto);
}
