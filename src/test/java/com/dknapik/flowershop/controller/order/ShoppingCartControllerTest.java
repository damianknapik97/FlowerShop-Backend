package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.order.FlowerOrderRepository;
import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.dto.order.ShoppingCartDto;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.FlowerOrder;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.product.Flower;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(value = "build/generated-snippets/shoppingcart")
@TestPropertySource(properties = {"app-monetary-currency=PLN", "app-debug-mode=false"})
public class ShoppingCartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ShoppingCartRepository repository;
    @Autowired
    private FlowerOrderRepository flowerOrderRepository;
    @Autowired
    private FlowerRepository flowerRepository;
    @Autowired
    private Environment env;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void cleanBeforeTest() {
        purgeDatabase();
    }

    @AfterEach
    public void cleanAfterTest() {
        purgeDatabase();
    }

    @Test
    public void getShoppingCartTest() throws Exception {
        /* Test configuration */
        int numberOfProducts = 10;
        String prefix = "Testing Product";
        ShoppingCartDto controlObject;

        /* Initialize entities needed in database */
        ShoppingCart shoppingCart = initializeShoppingCartEntity(prefix, numberOfProducts, false);
        Account account = createAccount("GetShoppingCartTest", "GetShoppingCartTest", shoppingCart);
        controlObject = convertToDto(shoppingCart);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                get("/shoppingcart").with(user(account.getName()));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response to expected dto  */
        ShoppingCartDto resultValue =
                objectMapper.readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        /* Cast Page to List, and compare it with previously created control value */
        Assertions.assertThat(resultValue).isEqualTo(controlObject);
    }

    @Test
    public void countShoppingCartProductsTest() throws Exception {
        /* Test configuration */
        int numberOfProducts = 10;
        String prefix = "Testing Product";
        String userName = "Test";

        /* Initialize entities needed in database */
        ShoppingCart shoppingCart = initializeShoppingCartEntity(prefix, numberOfProducts, false);
        Account account = createAccount(userName, "Test", shoppingCart);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                get("/shoppingcart/count").param("id", shoppingCart.getId().toString()).with(user(userName));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response to Integer  */

        int resultValue = objectMapper.readValue(result.getResponse().getContentAsString(), Integer.class);

        Assertions.assertThat(resultValue).isEqualTo(numberOfProducts);
    }


    public void purgeDatabase() {
        accountRepository.deleteAll();
        repository.deleteAll();
        flowerOrderRepository.deleteAll();
        flowerRepository.deleteAll();
    }

    /**
     * Creates, persists and returns Account entity.
     *
     * @param userName
     * @param password
     * @param shoppingCart
     * @return Persisted Account entity.
     */
    private Account createAccount(String userName, String password, ShoppingCart shoppingCart) {
        /* Create new account entity from provided arguments */
        Account newAccount = new Account(userName, userName + "@test.com", password);
        newAccount.setShoppingCart(shoppingCart);

        /* Persist created entity and return it */
        accountRepository.saveAndFlush(newAccount);
        return newAccount;
    }

    /**
     * Create and return Shopping Cart entity with initialized products with products orders inside it.
     *
     * @param productNamePrefix - name of the product that shopping cart will contain
     * @param numberOfProducts  - how many products should one shopping cart contain
     * @param saveToDatabase    - if shopping cart should be saved through repository to database.
     * @return - ShoppingCart Entity
     */
    private ShoppingCart initializeShoppingCartEntity(String productNamePrefix, int numberOfProducts, boolean saveToDatabase) {
        /* Create Shopping Cart entity from provided arguments */
        ShoppingCart shoppingCartEntity = new ShoppingCart("Testing Shopping Cart",
                new LinkedList<>(),
                new LinkedList<>(),
                initializeFlowerOrders(productNamePrefix, numberOfProducts),
                new LinkedList<>());

        /* Save created entity to database */
        if (saveToDatabase) {
            repository.saveAndFlush(shoppingCartEntity);
        }
        return shoppingCartEntity;
    }

    /**
     * @param numberOfEntities - how many entities should be generated
     * @return - List with NOT persisted Flower Order entities based on Flower entities.
     */
    private List<FlowerOrder> initializeFlowerOrders(String productPrefix, int numberOfEntities) {
        List<Flower> flowerList = initializeFlowers(productPrefix, numberOfEntities);
        List<FlowerOrder> flowerOrderList = new LinkedList<>();

        /* Create number of entities provided in function argument */
        for (int i = 0; i < numberOfEntities; i++) {
            flowerOrderList.add(new FlowerOrder(1, flowerList.get(i)));
        }

        /* Save created entities to database */
        // flowerOrderRepository.saveAll(flowerOrderList);
        // flowerOrderRepository.flush();

        return flowerOrderList;
    }

    /**
     * Create List with flowers and save it to database
     *
     * @param numberOfEntities - how many entities should be generated
     * @return List of persisted Flower Entities
     */
    private List<Flower> initializeFlowers(String productPrefix, int numberOfEntities) {
        List<Flower> flowerList = new LinkedList<>();
        MonetaryAmount money = Money.of(6.99, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Test Flower";

        /* Create number of entities provided in function argument */
        for (int i = 0; i < numberOfEntities; i++) {
            flowerList.add(new Flower(productPrefix.concat(String.valueOf(i)),
                    money, description.concat(String.valueOf(i)), 5));
        }

        /* Save created entities to database */
        flowerRepository.saveAll(flowerList);
        flowerRepository.flush();

        return flowerList;
    }

    /**
     * Maps provided object to entity.
     *
     * @param entity- entity for mapping
     * @return dto created from provided entity
     */
    private ShoppingCartDto convertToDto(ShoppingCart entity) {
        return mapper.map(entity, ShoppingCartDto.class);
    }

    /**
     * Manually convert Souvenir Dto to Entity because of MonetaryAmount attribute.
     *
     * @param dto - dto to map to entity
     * @return - mapped entity
     */
    private ShoppingCart convertToEntity(ShoppingCartDto dto) {
        return mapper.map(dto, ShoppingCart.class);
    }
}
