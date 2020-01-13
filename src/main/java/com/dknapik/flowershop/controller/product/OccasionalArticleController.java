package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.OccasionalArticleDto;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.services.product.OccasionalArticleService;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

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
     * Manually convert Entity to Dto cause ModelMapper won't properly handle MonetaryAmount interface
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    private OccasionalArticleDto convertToDto(OccasionalArticle entity) {
        return new OccasionalArticleDto(entity.getId(),
                entity.getName(),
                entity.getPrice().getNumber().numberValue(BigDecimal.class),
                entity.getPrice().getCurrency().getCurrencyCode(),
                entity.getDescription(),
                entity.getTheme());
    }

    /**
     * Manually convert Souvenir Dto to Entity because of MonetaryAmount attribute.
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    private OccasionalArticle convertToEntity(OccasionalArticleDto dto) {
        return new OccasionalArticle(dto.getId(),
                dto.getName(),
                Money.of(dto.getAmount(), dto.getCurrency()),
                dto.getDescription(),
                dto.getTheme()
        );
    }
}
