package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.services.product.OccasionalArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/occasionalarticle")
@CrossOrigin
public class OccasionalArticleController {
    private final OccasionalArticleService service;

    @Autowired
    public OccasionalArticleController(OccasionalArticleService service) {
        this.service = service;
    }

    @GetMapping
    public Page<OccasionalArticle> getOccasionalArticles(@RequestParam(value = "page", defaultValue = "0") int page) {
        return service.retrieveOccasionalArticlePage(page);
    }

}
