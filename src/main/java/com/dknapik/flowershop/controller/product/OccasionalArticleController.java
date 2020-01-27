package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.OccasionalArticleDto;
import com.dknapik.flowershop.mapper.product.ProductMapper;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.services.product.OccasionalArticleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/product/occasional-article")
@CrossOrigin
@Log4j2
public class OccasionalArticleController {
    private final OccasionalArticleService service;
    private final ProductMapper productMapper;

    @Autowired
    public OccasionalArticleController(OccasionalArticleService service, ProductMapper productMapper) {
        this.service = service;
        this.productMapper = productMapper;
    }

    @GetMapping
    public ResponseEntity<RestResponsePage<OccasionalArticleDto>> getOccasionalArticles(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        /* Retrieve desired page */
        log.info("Processing getOccasionalArticles request");
        RestResponsePage<OccasionalArticle> occasionalArticleRestResponsePage =
                service.retrieveOccasionalArticlePage(page);

        /* Cast retrieved entities to dto and create new RestResponsePage */
        log.info("Casting Entities to Dto");
        List<OccasionalArticleDto> occasionalArticleDtoList = new LinkedList<>();
        for (OccasionalArticle occasionalArticle : occasionalArticleRestResponsePage) {
            occasionalArticleDtoList.add(productMapper.convertToDto(occasionalArticle, OccasionalArticleDto.class));
        }

        log.info("Building response");
        RestResponsePage<OccasionalArticleDto> dtoRestResponsePage =
                new RestResponsePage<>(occasionalArticleDtoList, occasionalArticleRestResponsePage);
        return new ResponseEntity<>(dtoRestResponsePage, HttpStatus.OK);
    }
}
