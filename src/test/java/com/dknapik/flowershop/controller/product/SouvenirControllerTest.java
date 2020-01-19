package com.dknapik.flowershop.controller.product;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.product.SouvenirRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.product.SouvenirDto;
import com.dknapik.flowershop.model.product.Souvenir;
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
@AutoConfigureRestDocs(value = "build/generated-snippets/souvenir")
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
class SouvenirControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SouvenirRepository repository;
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

    @Test
    public void getSouvenirsTest() throws Exception {
        int numberOfEntities = 45;
        String prefix = "TestingSouvenir";
        int page = 2;
        RestResponsePage<SouvenirDto> controlPage;
        List<SouvenirDto> pageContent = new LinkedList<>();

        /* Initialize testing entities and create List representation of page that should be returned */
        List<Souvenir> designatedEntities = initializeSouvenirs(prefix, numberOfEntities);
        designatedEntities = designatedEntities.subList(ProductProperties.PAGE_SIZE * page, numberOfEntities);

        /* Create control value by mapping designated entities into dto*/
        for (Souvenir souvenir : designatedEntities) {
            pageContent.add(convertToDto(souvenir));
        }
        controlPage = new RestResponsePage<>(pageContent, PageRequest.of(2, ProductProperties.PAGE_SIZE), numberOfEntities);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                get("/product/souvenir").param("page", String.valueOf(page));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response to Page<Souvenir> data type */
        TypeReference<RestResponsePage<SouvenirDto>> typeReference =
                new TypeReference<RestResponsePage<SouvenirDto>>() {
                };
        RestResponsePage<SouvenirDto> resultValue =
                objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        /* Cast Page to List, and compare it with previously created control value */
        Assertions.assertThat(resultValue).containsExactlyInAnyOrderElementsOf(controlPage);

    }

    /**
     * Add desired number of Souvenir entities to database
     *
     * @param numberOfEntities - how many entities should be saved to database
     */
    private List<Souvenir> initializeSouvenirs(String namePrefix, int numberOfEntities) {
        List<Souvenir> souvenirList = new LinkedList<>();
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Test Article";


        Souvenir newEntity;
        while (numberOfEntities > 0) {
            newEntity = new Souvenir(namePrefix.concat(String.valueOf(numberOfEntities)), money, description);
            souvenirList.add(newEntity);
            repository.save(newEntity);
            numberOfEntities--;
        }

        repository.flush();
        return souvenirList;
    }


    /**
     * Convert Souvenir Entity to Dto using Model Mapper
     *
     * @param souvenir - entity for mapping
     * @return dto created from provided entity
     */
    private SouvenirDto convertToDto(Souvenir souvenir) {
        return modelMapper.map(souvenir, SouvenirDto.class);
    }

    /**
     * Convert Souvenir Dto to Entity using ModelMapper
     *
     * @param souvenirDto - dto to map to entity
     * @return - mapped entity
     */
    private Souvenir convertToEntity(SouvenirDto souvenirDto) {
        return modelMapper.map(souvenirDto, Souvenir.class);
    }
}