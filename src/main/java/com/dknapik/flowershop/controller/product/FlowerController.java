package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.FlowerDTO;
import com.dknapik.flowershop.mapper.product.ProductMapper;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.services.product.FlowerService;
import lombok.extern.log4j.Log4j2;
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
final class FlowerController {
    private final FlowerService service;
    private final ProductMapper productMapper;


    @Autowired
    FlowerController(FlowerService service, ProductMapper productMapper) {
        this.service = service;
        this.productMapper = productMapper;
    }

    @GetMapping
    ResponseEntity<RestResponsePage<FlowerDTO>> getFlowers(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        log.traceEntry();

        /* Retrieve desired page */
        RestResponsePage<Flower> flowerResponsePage = service.retrieveFlowerPage(page);

        /* Cast retrieved entities to dto and create new RestResponsePage */
        log.info("Casting Entities to Dto");
        List<FlowerDTO> flowerDtoList = new LinkedList<>();
        for (Flower f : flowerResponsePage) {
            flowerDtoList.add(productMapper.convertToDto(f, FlowerDTO.class));
        }

        RestResponsePage<FlowerDTO> dtoRestResponsePage = new RestResponsePage<>(flowerDtoList, flowerResponsePage);

        log.traceExit();
        return new ResponseEntity<>(dtoRestResponsePage, HttpStatus.OK);
    }
}
