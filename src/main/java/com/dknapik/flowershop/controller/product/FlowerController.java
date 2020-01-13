package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.FlowerDto;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.services.product.FlowerService;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/product/flower")
@CrossOrigin
public class FlowerController {
    private final FlowerService service;


    @Autowired
    public FlowerController(FlowerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<RestResponsePage<FlowerDto>> getFlowers(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        /* Retrieve desired page */
        RestResponsePage<Flower> flowerResponsePage = service.retrieveFlowerPage(page);

        /* Cast retrieved entities to dto and create new RestResponsePage */
        List<FlowerDto> flowerDtoList = new LinkedList<>();
        for (Flower f : flowerResponsePage) {
            flowerDtoList.add(convertToDto(f));
        }
        RestResponsePage<FlowerDto> dtoRestResponsePage = new RestResponsePage<>(flowerDtoList, flowerResponsePage);

        return new ResponseEntity<>(dtoRestResponsePage, HttpStatus.OK);
    }

    /**
     * Manually convert Entity to Dto cause ModelMapper won't properly handle MonetaryAmount interface
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    private FlowerDto convertToDto(Flower entity) {
        return new FlowerDto(entity.getId(),
                entity.getName(),
                entity.getPrice().getNumber().numberValue(BigDecimal.class),
                entity.getPrice().getCurrency().getCurrencyCode(),
                entity.getDescription(),
                entity.getHeight());
    }

    /**
     * Manually convert Souvenir Dto to Entity because of MonetaryAmount attribute.
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    private Flower convertToEntity(FlowerDto dto) {
        return new Flower(dto.getId(),
                dto.getName(),
                Money.of(dto.getAmount(), dto.getCurrency()),
                dto.getDescription(),
                dto.getHeight()
        );
    }
}
