package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.constants.ProductMessage;
import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.SouvenirRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.product.Souvenir;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public final class SouvenirService {
    private final SouvenirRepository repository;

    @Autowired
    public SouvenirService(SouvenirRepository repository) {
        this.repository = repository;
    }

    /**
     * Searched database for single product instance and returns it.
     *
     * @param id - product id
     * @return Souvenir entity with provided id;
     */
    public Souvenir retrieveSingleSouvenir(UUID id) {
        log.traceEntry();

        Optional<Souvenir> retrievedEntity = repository.findById(id);

        log.traceExit();
        return retrievedEntity.orElseThrow(() -> new ResourceNotFoundException(ProductMessage.PRODUCT_NOT_FOUND));
    }

    /**
     * Retrieve Page of Souvenir products
     * By default page consist of 20 products
     *
     * @param pageNumber - which page to return
     * @return Page with Souvenir products
     */
    public RestResponsePage<Souvenir> retrieveSouvenirPage(int pageNumber) {
        log.traceEntry();

        /* Create Page request for repository */
        Pageable pageable = PageRequest.of(pageNumber, ProductProperties.PAGE_SIZE);

        /* Retrieve page of products from repository and cast it to list */
        List<Souvenir> content = repository.findAll(pageable).getContent();

        /* Return collection of products ready for trasnport/serialization/mapping */
        log.traceExit();
        return new RestResponsePage<>(content, pageable, repository.count());
    }
}
