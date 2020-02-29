package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.SouvenirDTO;
import com.dknapik.flowershop.mapper.ProductMapper;
import com.dknapik.flowershop.model.product.Souvenir;
import com.dknapik.flowershop.services.product.SouvenirService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/product/souvenir")
@CrossOrigin
@Log4j2
final class SouvenirController {
    private final SouvenirService service;
    private final ProductMapper productMapper;


    @Autowired
    SouvenirController(SouvenirService service, ProductMapper productMapper) {
        this.service = service;
        this.productMapper = productMapper;
    }

    /**
     * Retrieve unsorted page of Souvenir prooducts
     *
     * @param page - number of page to return (By default page contains 20 objects)
     * @return Page with Souvenir products
     */
    @GetMapping
    ResponseEntity<RestResponsePage<SouvenirDTO>> getSouvenirs(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        log.traceEntry();

        /* Retrieve Page of Souvenir entities */
        RestResponsePage<Souvenir> souvenirs = service.retrieveSouvenirPage(page);

        /* Convert content to Dto */
        log.debug("Casting Entities to Dto");
        List<SouvenirDTO> souvenirDtoList = new LinkedList<>();
        for (Souvenir souvenir : souvenirs) {
            souvenirDtoList.add(productMapper.convertToDto(souvenir, SouvenirDTO.class));
        }

        /* Build Response entity and respond */
        RestResponsePage<SouvenirDTO> souvenirDtos = new RestResponsePage<>(souvenirDtoList, souvenirs);

        log.traceExit();
        return new ResponseEntity<>(souvenirDtos, HttpStatus.OK);
    }
}
