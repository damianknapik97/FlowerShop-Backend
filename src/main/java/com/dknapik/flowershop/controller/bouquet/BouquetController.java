package com.dknapik.flowershop.controller.bouquet;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.constants.sorting.ProductSortingProperty;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.bouquet.BouquetDTO;
import com.dknapik.flowershop.mapper.BouquetMapper;
import com.dknapik.flowershop.model.bouquet.Bouquet;
import com.dknapik.flowershop.services.bouquet.BouquetService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bouquet")
@ToString
@Log4j2
public final class BouquetController {
    private final BouquetService bouquetService;
    private final BouquetMapper bouquetMapper;

    @Autowired
    public BouquetController(BouquetService bouquetService, BouquetMapper bouquetMapper) {
        this.bouquetService = bouquetService;
        this.bouquetMapper = bouquetMapper;
    }

    /**
     * Extracts page of sorted bouquet DTOs from database and returns it with OK status.
     *
     * @param pageNumber      - which page should be retrieved
     * @param sortingProperty - which sorting should be used
     * @return Payload with page of Bouquet DTOs
     */
    @GetMapping("/page")
    ResponseEntity<RestResponsePage<BouquetDTO>> bouquetPage(
            @RequestParam(name = "number", defaultValue = "0") int pageNumber,
            @RequestParam(name = "sorting", defaultValue = "NONE") ProductSortingProperty sortingProperty) {
        log.traceEntry();

        RestResponsePage<Bouquet> bouquetResponsePage =
                bouquetService.retrieveBouquetsPage(pageNumber, ProductProperties.PAGE_SIZE, sortingProperty);
        RestResponsePage<BouquetDTO> bouquetResponseDTO =
                new RestResponsePage<>(bouquetMapper.mapListToDTO(bouquetResponsePage.getContent()),
                        bouquetResponsePage);

        log.traceExit();
        return new ResponseEntity<>(bouquetResponseDTO, HttpStatus.OK);
    }
}
