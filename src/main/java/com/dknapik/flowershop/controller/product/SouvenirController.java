package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.model.product.Souvenir;
import com.dknapik.flowershop.services.product.SouvenirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/souvenir")
@CrossOrigin

public class SouvenirController {
    private final SouvenirService service;

    @Autowired
    public SouvenirController(SouvenirService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<RestResponsePage<Souvenir>> getSouvenirs(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(service.retrieveSouvenirPage(page), HttpStatus.OK);
    }
}
