package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.model.product.Flower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FlowerService {
    private final FlowerRepository repository;

    @Autowired
    public FlowerService(FlowerRepository repository) {
        this.repository = repository;
    }

    public Page<Flower> retrieveFlowerPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, ProductProperties.PAGE_SIZE);

        return repository.findAll(pageable);
    }
}
