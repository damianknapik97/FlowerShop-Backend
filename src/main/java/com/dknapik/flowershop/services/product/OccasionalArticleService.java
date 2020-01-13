package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OccasionalArticleService {
    private final OccasionalArticleRepository repository;


    @Autowired
    public OccasionalArticleService(OccasionalArticleRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieve page of products
     */
    public RestResponsePage<OccasionalArticle> retrieveOccasionalArticlePage(int pageNumber) {
        /* Create Page request for repository */
        Pageable pageable = PageRequest.of(pageNumber, ProductProperties.PAGE_SIZE);

        /* Retrieve page of products from repository and cast it to list */
        List<OccasionalArticle> content = repository.findAll(pageable).getContent();

        /* Return collection of products ready for trasnport/serialization/mapping */
        return new RestResponsePage<>(content, pageable, repository.count());
    }

}
