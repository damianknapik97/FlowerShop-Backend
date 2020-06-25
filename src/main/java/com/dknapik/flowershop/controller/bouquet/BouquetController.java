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

import javax.money.MonetaryAmount;
import java.util.Map;
import java.util.UUID;

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
    public ResponseEntity<RestResponsePage<BouquetDTO>> bouquetPage(
            @RequestParam(name = "number", defaultValue = "0") int pageNumber,
            @RequestParam(name = "sorting", defaultValue = "NONE") ProductSortingProperty sortingProperty) {
        log.traceEntry();

        /* Extract bouquet page */
        RestResponsePage<Bouquet> bouquetResponsePage =
                bouquetService.retrieveBouquetsPage(pageNumber, ProductProperties.PAGE_SIZE, sortingProperty);

        /* Map bouquet page to bouquet DTO page */
        RestResponsePage<BouquetDTO> bouquetResponseDTO =
                new RestResponsePage<>(bouquetMapper.mapListToDTO(bouquetResponsePage.getContent()),
                        bouquetResponsePage);

        /* Count total price for each bouquet and map them to bouquet DTO page */
        Map<UUID, MonetaryAmount> bouquetPrices =
                bouquetService.countIndividualBouquetPrices(bouquetResponsePage.getContent());
        bouquetMapper.mapBouquetPricesToDTO(bouquetResponseDTO.getContent(), bouquetPrices);

        log.traceExit();
        return new ResponseEntity<>(bouquetResponseDTO, HttpStatus.OK);
    }
}
