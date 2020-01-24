package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.OccasionalArticleDto;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.services.product.OccasionalArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/product/occasional-article")
@CrossOrigin
public class OccasionalArticleController {
    private final OccasionalArticleService service;
    private final ModelMapper modelMapper;


    @Autowired
    public OccasionalArticleController(OccasionalArticleService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<RestResponsePage<OccasionalArticleDto>> getFlowers(
            @RequestParam(value = "page", defaultValue = "0") int page) {
        /* Retrieve desired page */
        RestResponsePage<OccasionalArticle> occasionalArticleRestResponsePage =
                service.retrieveOccasionalArticlePage(page);

        /* Cast retrieved entities to dto and create new RestResponsePage */
        List<OccasionalArticleDto> occasionalArticleDtoList = new LinkedList<>();
        for (OccasionalArticle occasionalArticle : occasionalArticleRestResponsePage) {
            occasionalArticleDtoList.add(convertToDto(occasionalArticle));
        }
        RestResponsePage<OccasionalArticleDto> dtoRestResponsePage =
                new RestResponsePage<>(occasionalArticleDtoList, occasionalArticleRestResponsePage);

        return new ResponseEntity<>(dtoRestResponsePage, HttpStatus.OK);
    }

    /**
     * Convert Entity to Dto using Model Mapper
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    private OccasionalArticleDto convertToDto(OccasionalArticle entity) {
        return modelMapper.map(entity, OccasionalArticleDto.class);
    }

    /**
     * Convert Souvenir Dto to Entity using Model Mapper
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    private OccasionalArticle convertToEntity(OccasionalArticleDto dto) {
        return modelMapper.map(dto, OccasionalArticle.class);
    }
}
