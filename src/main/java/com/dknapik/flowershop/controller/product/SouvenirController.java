package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.SouvenirDto;
import com.dknapik.flowershop.model.product.Souvenir;
import com.dknapik.flowershop.services.product.SouvenirService;
import org.javamoney.moneta.Money;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/product/souvenir")
@CrossOrigin

public class SouvenirController {
    private final SouvenirService service;
    private final ModelMapper modelMapper;


    @Autowired
    public SouvenirController(SouvenirService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    /**
     * Retrieve unsorted page of Souvenir prooducts
     *
     * @param page - number of page to return (By default page contains 20 objects)
     * @return Page with Souvenir products
     */
    @GetMapping
    public ResponseEntity<RestResponsePage<SouvenirDto>> getSouvenirs(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        /* Retrieve Page of Souvenir entities */
        RestResponsePage<Souvenir> souvenirs = service.retrieveSouvenirPage(page);

        /* Convert content to Dto */
        List<SouvenirDto> souvenirDtoList = new LinkedList<>();
        for (Souvenir souvenir : souvenirs) {
            souvenirDtoList.add(convertToDto(souvenir));
        }

        /* Build Response entity and respond */
        RestResponsePage<SouvenirDto> souvenirDtos = new RestResponsePage<>(souvenirDtoList, souvenirs);
        return new ResponseEntity<>(souvenirDtos, HttpStatus.OK);
    }

    /**
     * Convert Souvenir Entity to Dto using Model Mapper
     *
     * @param souvenir - entity for mapping
     * @return dto created from provided entity
     */
    private SouvenirDto convertToDto(Souvenir souvenir) {
        return modelMapper.map(souvenir, SouvenirDto.class);
    }

    /**
     * Convert Souvenir Dto to Entity using ModelMapper
     *
     * @param souvenirDto - dto to map to entity
     * @return - mapped entity
     */
    private Souvenir convertToEntity(SouvenirDto souvenirDto) {
        return modelMapper.map(souvenirDto, Souvenir.class);
    }
}
