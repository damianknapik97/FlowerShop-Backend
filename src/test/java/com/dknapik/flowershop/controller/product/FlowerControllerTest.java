package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.FlowerDTO;
import com.dknapik.flowershop.model.product.Flower;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
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
import java.util.LinkedList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(value = "build/generated-snippets/souvenir")
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class FlowerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FlowerRepository repository;
    @Autowired
    private Environment env;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void purgeDatabase() {
        repository.deleteAll();
    }

    /* TODO: Values returned are not the ones that are expected (around 2 pages later) - investigate  */
    //@Test
    public void getFlowersTest() throws Exception {
        int numberOfEntities = 45;
        String prefix = "Testing Flower";
        int page = 0;
        RestResponsePage<FlowerDTO> controlCollection;


        /* Initialize testing entities and create List with entities that should be in http response */
        List<Flower> flowerList = initializeEntities(prefix, numberOfEntities)
                .subList(ProductProperties.PAGE_SIZE * page, ProductProperties.PAGE_SIZE * page + ProductProperties.PAGE_SIZE);
        List<FlowerDTO> content = castCollectionToDto(flowerList);

        controlCollection =
                new RestResponsePage<>(content, PageRequest.of(page, ProductProperties.PAGE_SIZE), numberOfEntities);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                get("/product/flower").param("page", String.valueOf(page));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response to Page<Souvenir> data type */
        TypeReference<RestResponsePage<FlowerDTO>> typeReference =
                new TypeReference<RestResponsePage<FlowerDTO>>() {
                };
        RestResponsePage<FlowerDTO> resultValue =
                objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        /* Cast Page to List, and compare it with previously created control value */
        Assertions.assertThat(resultValue).containsExactlyInAnyOrderElementsOf(controlCollection);
    }

    /**
     * Add desired number of entities for use in tests.
     * Entities are persisted in provided database.
     *
     * @return List with initialized and persisted entities
     */
    private List<Flower> initializeEntities(String namePrefix, int numberOfEntities) {
        List<Flower> entityList = new LinkedList<>();
        MonetaryAmount money = Money.of(6.99, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Test Flower";

        /* Create number of entities provided in function argument */
        for (int i = 0; i < numberOfEntities; i++) {
            entityList.add(new Flower(namePrefix.concat(String.valueOf(i)),
                    money, description.concat(String.valueOf(i)), 5));
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
    private List<FlowerDTO> castCollectionToDto(Iterable<Flower> collection) {
        List<FlowerDTO> returnCollection = new LinkedList<>();

        for (Flower flower : collection) {
            returnCollection.add(convertToDto(flower));
        }

        return returnCollection;
    }

    /**
     * Convert Entity to Dto using Model Mapper
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    private FlowerDTO convertToDto(Flower entity) {
        return modelMapper.map(entity, FlowerDTO.class);
    }

    /**
     * Convert Souvenir Dto to Entity using Model Mapper
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    private Flower convertToEntity(FlowerDTO dto) {
        return modelMapper.map(dto, Flower.class);
    }

}
