package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.FlowerDto;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.services.product.FlowerService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/product/flower")
@CrossOrigin
@Log4j2
public class FlowerController {
    private final FlowerService service;
    private final ModelMapper modelMapper;


    @Autowired
    public FlowerController(FlowerService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<RestResponsePage<FlowerDto>> getFlowers(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        log.info("Processing getFlowers request");
        /* Retrieve desired page */
        RestResponsePage<Flower> flowerResponsePage = service.retrieveFlowerPage(page);

        /* Cast retrieved entities to dto and create new RestResponsePage */
        log.info("Casting Entities to Dto");
        List<FlowerDto> flowerDtoList = new LinkedList<>();
        for (Flower f : flowerResponsePage) {
            flowerDtoList.add(convertToDto(f));
        }

        log.info("Building response");
        RestResponsePage<FlowerDto> dtoRestResponsePage = new RestResponsePage<>(flowerDtoList, flowerResponsePage);
        return new ResponseEntity<>(dtoRestResponsePage, HttpStatus.OK);
    }

    /**
     * Convert Entity to Dto using Model Mapper
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    private FlowerDto convertToDto(Flower entity) {
        return modelMapper.map(entity, FlowerDto.class);
    }

    /**
     * Convert Souvenir Dto to Entity using Model Mapper
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    private Flower convertToEntity(FlowerDto dto) {
        return modelMapper.map(dto, Flower.class);
    }
}
