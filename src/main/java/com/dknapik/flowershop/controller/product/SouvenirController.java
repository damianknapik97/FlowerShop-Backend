package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.model.product.Souvenir;
import com.dknapik.flowershop.services.product.SouvenirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/souvenir")
@CrossOrigin
public class SouvenirController {
    private final SouvenirService service;

    @Autowired
    public SouvenirController(SouvenirService service) {
        this.service = service;
    }

    public Page<Souvenir> getSouvenirs(@RequestParam(value = "page", defaultValue = "0") int page) {
        return service.retrieveSouvenirPage(page);
    }
}
