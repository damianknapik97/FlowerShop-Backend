package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<OccasionalArticle> retrieveOccasionalArticlePage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, ProductProperties.PAGE_SIZE);

        return repository.findAll(pageable);
    }

}
