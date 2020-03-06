package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.constants.ProductMessage;
import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.product.Flower;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public final class FlowerService {
    private final FlowerRepository repository;

    @Autowired
    public FlowerService(FlowerRepository repository) {
        this.repository = repository;
    }

    /**
     * Searched database for single product instance and returns it.
     *
     * @param id - product id
     * @return Flower entity with provided id;
     */
    public Flower retrieveSingleFlower(UUID id) {
        log.traceEntry();

        Optional<Flower> retrievedFlower = repository.findById(id);
        if (!retrievedFlower.isPresent()) {
            log.throwing(Level.WARN, new ResourceNotFoundException(ProductMessage.PRODUCT_NOT_FOUND));
            throw new ResourceNotFoundException(ProductMessage.PRODUCT_NOT_FOUND);
        }

        log.traceExit();
        return retrievedFlower.get();
    }

    /**
     * Retrieve Page of Flower products
     * By default page consist of 20 products
     *
     * @param pageNumber - which page to return
     * @return Page with Flower products
     */
    public RestResponsePage<Flower> retrieveFlowerPage(int pageNumber) {
        log.traceEntry();

        /* Create Page request for repository */
        Pageable pageable = PageRequest.of(pageNumber, ProductProperties.PAGE_SIZE);

        /* Retrieve page of products from repository and cast it to list */
        List<Flower> content = repository.findAll(pageable).getContent();

        /* Return collection of products ready for trasnport/serialization/mapping */
        log.traceExit();
        return new RestResponsePage<>(content, pageable, repository.count());
    }
}
