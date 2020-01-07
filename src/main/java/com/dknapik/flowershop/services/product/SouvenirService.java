package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.SouvenirRepository;
import com.dknapik.flowershop.model.product.Souvenir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SouvenirService {
    private final SouvenirRepository repository;

    @Autowired
    public SouvenirService(SouvenirRepository repository) {
        this.repository = repository;
    }

    public Page<Souvenir> retrieveSouvenirPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, ProductProperties.PAGE_SIZE);

        return repository.findAll(pageable);
    }
}
