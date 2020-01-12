package com.dknapik.flowershop.services.product;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.SouvenirRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.model.product.Souvenir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SouvenirService {
    private final SouvenirRepository repository;

    @Autowired
    public SouvenirService(SouvenirRepository repository) {
        this.repository = repository;
    }

    public RestResponsePage<Souvenir> retrieveSouvenirPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, ProductProperties.PAGE_SIZE);
        List<Souvenir> content = repository.findAll(pageable).getContent();

        return new RestResponsePage<>(content, pageable, repository.count());
    }
}
