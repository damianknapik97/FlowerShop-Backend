package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.services.product.FlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/flowers")
@CrossOrigin
public class FlowerController {
    private final FlowerService service;

    @Autowired
    public FlowerController(FlowerService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Flower> getFlowers(@RequestParam(value = "page", defaultValue = "0") int page) {
        return service.retrieveFlowerPage(page);
    }
}
