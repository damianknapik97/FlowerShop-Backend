package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.constants.ProductMessage;
import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.product.OccasionalArticle;
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
public final class OccasionalArticleService {
    private final OccasionalArticleRepository repository;


    @Autowired
    public OccasionalArticleService(OccasionalArticleRepository repository) {
        this.repository = repository;
    }

    /**
     * Searched database for single product instance and returns it.
     *
     * @param id - product id
     * @return Occasional Article entity with provided id;
     */
    public OccasionalArticle retrieveSingleOccasionalArticle(UUID id) {
        log.traceEntry();

        Optional<OccasionalArticle> retrievedEntity = repository.findById(id);

        log.traceExit();
        return retrievedEntity.orElseThrow(() -> new ResourceNotFoundException(ProductMessage.PRODUCT_NOT_FOUND));
    }


    /**
     * Retrieve page of products
     */
    public RestResponsePage<OccasionalArticle> retrieveOccasionalArticlePage(int pageNumber) {
        log.traceEntry();

        /* Create Page request for repository */
        Pageable pageable = PageRequest.of(pageNumber, ProductProperties.PAGE_SIZE);

        /* Retrieve page of products from repository and cast it to list */
        List<OccasionalArticle> content = repository.findAll(pageable).getContent();

        /* Return collection of products ready for trasnport/serialization/mapping */
        log.traceExit();
        return new RestResponsePage<>(content, pageable, repository.count());
    }

}
