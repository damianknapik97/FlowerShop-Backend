package com.dknapik.flowershop.services.bouquet;

import com.dknapik.flowershop.constants.ProductMessage;
import com.dknapik.flowershop.constants.sorting.ProductSortingProperty;
import com.dknapik.flowershop.database.bouquet.BouquetRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.bouquet.Bouquet;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@ToString
@Log4j2
public final class BouquetService {
    private final BouquetRepository bouquetRepository;

    @Autowired
    public BouquetService(BouquetRepository bouquetRepository) {
        this.bouquetRepository = bouquetRepository;
    }


    /**
     * Searches database for provided Biyqyet ID, and returns related to it entity.
     * After search for mentioned ID fails, ResourceNotFound runtime exception is thrown.
     */
    public Bouquet retrieveBouquet(UUID bouquetID) {
        log.traceEntry();

        Optional<Bouquet> bouquetOptional = bouquetRepository.findById(bouquetID);
        if (!bouquetOptional.isPresent()) {
            log.throwing(new ResourceNotFoundException(ProductMessage.PRODUCT_NOT_FOUND));
            throw new ResourceNotFoundException(ProductMessage.PRODUCT_NOT_FOUND);

        }

        log.traceExit();
        return bouquetOptional.get();
    }

    /**
     * Creates ResponsePage containing sorted Bouquet entities.
     *
     * @param pageNumber      - which page should be retrieved
     * @param pageSize        - how large should the page be.
     * @param sortingProperty - how orders should be ordered when retrieved from database
     * @return - RestResponsePage with Order entities.
     */
    public RestResponsePage<Bouquet> retrieveBouquetsPage(int pageNumber, int pageSize,
                                                          @NonNull ProductSortingProperty sortingProperty) {
        log.traceEntry();

        /* Create Page request for repository and retrieve it. */
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Bouquet> content = retrieveSortedPage(pageable, sortingProperty);

        /* Return collection of products ready for transport/serialization/mapping */
        log.traceExit();
        return new RestResponsePage<>(content);
    }

    /**
     * Determines which sorting method is desired from provided enum, calls according repository function containing
     * suitable query, and returns Page object containing Bouquet entities inside.
     * If no property matches currently defined ones in this function, default Spring sorting is used.
     *
     * @param pageable        - Page request used in repository for page retrieval
     * @param sortingProperty - Enum containing name of desired sorting method
     * @return Page with Bouquets
     */
    private Page<Bouquet> retrieveSortedPage(@NonNull Pageable pageable,
                                             @NonNull ProductSortingProperty sortingProperty) {
        log.traceEntry();

        Page<Bouquet> bouquetPage;
        switch (sortingProperty) {
            case NAME_ASCENDING:
                bouquetPage = bouquetRepository.findAllByOrderByNameAsc(pageable);
                break;
            case NAME_DESCENDING:
                bouquetPage = bouquetRepository.findAllByOrderByNameDesc(pageable);
                break;
            case PRICE_DESCENDING:
                bouquetPage = bouquetRepository.findAllByOrderByProductionCostDesc(pageable);
                break;
            case PRICE_ASCENDING:
                bouquetPage = bouquetRepository.findAllByOrderByProductionCostAsc(pageable);
                break;
            default:
                bouquetPage = bouquetRepository.findAll(pageable);
        }

        log.traceExit();
        return bouquetPage;
    }

}
