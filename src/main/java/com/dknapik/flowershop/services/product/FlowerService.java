package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.model.product.Flower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowerService {
    private final FlowerRepository repository;

    @Autowired
    public FlowerService(FlowerRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieve Page of Flower products
     * By default page consist of 20 products
     *
     * @param pageNumber - which page to return
     * @return Page with Flower products
     */
    public RestResponsePage<Flower> retrieveFlowerPage(int pageNumber) {
        /* Create Page request for repository */
        Pageable pageable = PageRequest.of(pageNumber, ProductProperties.PAGE_SIZE);

        /* Retrieve page of products from repository and cast it to list */
        List<Flower> content = repository.findAll(pageable).getContent();

        /* Return collection of products ready for trasnport/serialization/mapping */
        return new RestResponsePage<>(content, pageable, repository.count());
    }
}
