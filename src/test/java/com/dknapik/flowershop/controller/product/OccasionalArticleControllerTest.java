package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.order.OccasionalArticleOrderRepository;
import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.OccasionalArticleDto;
import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(value = "build/generated-snippets/occasionalarticle")
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
class OccasionalArticleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OccasionalArticleRepository repository;
    @Autowired
    private OccasionalArticleOrderRepository orderRepository;
    @Autowired
    private Environment env;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void purgeDatabase() {
        /* Initialized by Seeders, order entities must be deleted for products to be removable */
        orderRepository.deleteAll();
        repository.deleteAll();
    }


    @Test
    public void getOccasionalArticlesTest() throws Exception {
        int numberOfEntities = 45;
        String prefix = "Testing Flower";
        int page = 0;
        RestResponsePage<OccasionalArticleDto> controlCollection;


        /* Initialize testing entities and create List with entities that should be in http response */
        List<OccasionalArticle> articleList = initializeEntities(prefix, numberOfEntities)
                .subList(0, ProductProperties.PAGE_SIZE);
        List<OccasionalArticleDto> content = castCollectionToDto(articleList);
        controlCollection =
                new RestResponsePage<>(content, PageRequest.of(page, ProductProperties.PAGE_SIZE), numberOfEntities);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                get("/product/occasionalarticle").param("page", String.valueOf(page));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response to Page<Souvenir> data type */
        TypeReference<RestResponsePage<OccasionalArticleDto>> typeReference =
                new TypeReference<RestResponsePage<OccasionalArticleDto>>() {
                };
        RestResponsePage<OccasionalArticleDto> resultValue =
                objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        /* Cast Page to List, and compare it with previously created control value */
        Assertions.assertThat(resultValue).
                usingElementComparatorIgnoringFields("amount")  // Ignoring Amount field because of different number format
                .containsExactlyInAnyOrderElementsOf(controlCollection);
    }


    /**
     * Add desired number of entities for use in tests.
     * Entities are persisted in provided database.
     *
     * @return List with initialized and persisted entities
     */
    private List<OccasionalArticle> initializeEntities(String namePrefix, int numberOfEntities) {
        List<OccasionalArticle> entityList = new LinkedList<>();
        MonetaryAmount money = Money.of(3.01, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Test Occasional Article";

        /* Create number of entities provided in function argument */
        while (numberOfEntities > 0) {
            entityList.add(new OccasionalArticle(namePrefix.concat(String.valueOf(numberOfEntities)),
                    money, description.concat(String.valueOf(numberOfEntities)), "TestTheme"));
            numberOfEntities--;
        }

        /* Save created entities to database */
        repository.saveAll(entityList);
        repository.flush();

        return entityList;
    }

    /**
     * Cast provided entity collection to collection with dto
     *
     * @param collection - iterable collection containing Flower entities
     * @return list with FlowerDto objects
     */
    private List<OccasionalArticleDto> castCollectionToDto(Iterable<OccasionalArticle> collection) {
        List<OccasionalArticleDto> returnCollection = new LinkedList<>();

        for (OccasionalArticle article : collection) {
            returnCollection.add(convertToDto(article));
        }

        return returnCollection;
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