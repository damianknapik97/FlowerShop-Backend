package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.ShoppingCartMessage;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.bouquet.BouquetRepository;
import com.dknapik.flowershop.database.order.FlowerOrderRepository;
import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.database.product.SouvenirRepository;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.order.ShoppingCartDTO;
import com.dknapik.flowershop.mapper.order.ShoppingCartMapper;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.productorder.FlowerOrder;
import com.dknapik.flowershop.model.productorder.OccasionalArticleOrder;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.productorder.SouvenirOrder;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.model.product.Souvenir;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.transaction.annotation.Transactional;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(value = "build/generated-snippets/shopping-cart")
@TestPropertySource(properties = {"app-monetary-currency=PLN", "app-debug-mode=false"})
@Transactional
class ShoppingCartControllerTest {
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
    private OccasionalArticleRepository occasionalArticleRepository;
    @Autowired
    private SouvenirRepository souvenirRepository;
    @Autowired
    private BouquetRepository bouquetRepository;
    @Autowired
    private Environment env;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanBeforeTest() {
        purgeDatabase();
    }

    @AfterEach
    void cleanAfterTest() {
        purgeDatabase();
    }

    @Test
    void getShoppingCartTest() throws Exception {
        /* Test configuration */
        int numberOfProducts = 10;
        String prefix = "Testing Product";
        ShoppingCartDTO controlObject;
        String url = "/shopping-cart";

        /* Initialize entities needed in database */
        ShoppingCart shoppingCart = initializeShoppingCartEntity(prefix, numberOfProducts, false);
        Account account = createAccount("GetShoppingCartTest", "GetShoppingCartTest", shoppingCart);
        controlObject = shoppingCartMapper.mapToDTO(shoppingCart);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                get(url).with(user(account.getName()));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response to expected dto  */
        ShoppingCartDTO resultValue =
                objectMapper.readValue(result.getResponse().getContentAsString(), ShoppingCartDTO.class);

        /* Cast Page to List, and compare it with previously created control value */
        Assertions.assertThat(resultValue).isEqualToIgnoringNullFields(controlObject);
    }

    @Test
    void countShoppingCartProductsTest() throws Exception {
        /* Test configuration */
        int numberOfProducts = 10;
        String prefix = "Testing Product";
        String userName = "Test";
        String url = "/shopping-cart/count";

        /* Initialize entities needed in database */
        ShoppingCart shoppingCart = initializeShoppingCartEntity(prefix, numberOfProducts, false);
        Account account = createAccount(userName, "Test", shoppingCart);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                get(url).param("id", shoppingCart.getId().toString()).with(user(userName));

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

    @Test
    void putFlowerOrderTest() throws Exception {
        /* Initialize entities needed in database */
        String userName = "Test";
        String url = "/shopping-cart/flower";
        ShoppingCart shoppingCart = initializeShoppingCartEntity("", 0, false);
        Account account = createAccount(userName, "GetShoppingCartTest", shoppingCart);
        Flower testingProduct = initializeFlowers("FlowerToSave", 1).get(0);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                put(url).param("id", testingProduct.getId().toString()).with(user(userName));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        MessageResponseDTO messageResponseDTO =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);
        if (!messageResponseDTO.getMessage().equals(ShoppingCartMessage.PRODUCT_ADDED_SUCCESSFULLY.toString())) {
            throw new RuntimeException("Unexpected response value");
        }

        /* Retrieve shopping cart again from the database, to check if the new product was added */
        ShoppingCart afterTestResults = repository.getOne(shoppingCart.getId());
        Assertions.assertThat(afterTestResults.getFlowerOrderList().get(0).getFlower())
                .isEqualToComparingFieldByField(testingProduct);

    }

    @Test
    void putOccasionalArticleOrderTest() throws Exception {
        /* Initialize entities needed in database */
        String userName = "Test";
        String url = "/shopping-cart/occasional-article";
        MonetaryAmount money = Money.of(6.99, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        ShoppingCart shoppingCart = initializeShoppingCartEntity("", 0, false);
        Account account = createAccount(userName, "GetShoppingCartTest", shoppingCart);
        OccasionalArticle testingProduct =
                new OccasionalArticle("Testing Product", money, "Testing Product", "Testing Product");
        occasionalArticleRepository.saveAndFlush(testingProduct);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                put(url).param("id", testingProduct.getId().toString()).with(user(userName));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        MessageResponseDTO messageResponseDTO =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);
        if (!messageResponseDTO.getMessage().equals(ShoppingCartMessage.PRODUCT_ADDED_SUCCESSFULLY.toString())) {
            throw new RuntimeException("Unexpected response value");
        }

        /* Retrieve shopping cart again from the database, to check if the new product was added */
        ShoppingCart afterTestResults = repository.getOne(shoppingCart.getId());
        Assertions.assertThat(afterTestResults.getOccasionalArticleOrderList().get(0).getOccasionalArticle())
                .isEqualToComparingFieldByField(testingProduct);
    }

    @Test
    void putSouvenirOrderTest() throws Exception {
        /* Initialize entities needed in database */
        String userName = "Test";
        String url = "/shopping-cart/souvenir";
        MonetaryAmount money = Money.of(6.99, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        ShoppingCart shoppingCart = initializeShoppingCartEntity("", 0, false);
        Account account = createAccount(userName, "GetShoppingCartTest", shoppingCart);
        Souvenir testingProduct = new Souvenir("Testing Product", money, "Testing Product");
        souvenirRepository.saveAndFlush(testingProduct);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                put(url).param("id", testingProduct.getId().toString()).with(user(userName));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        MessageResponseDTO messageResponseDTO =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);
        if (!messageResponseDTO.getMessage().equals(ShoppingCartMessage.PRODUCT_ADDED_SUCCESSFULLY.toString())) {
            throw new RuntimeException("Unexpected response value");
        }

        /* Retrieve shopping cart again from the database, to check if the new product was added */
        ShoppingCart afterTestResults = repository.getOne(shoppingCart.getId());
        Assertions.assertThat(afterTestResults.getSouvenirOrderList().get(0).getSouvenir())
                .isEqualToComparingFieldByField(testingProduct);

    }

    @Test
    void removeFlowerOrderFromShoppingCartTest() throws Exception {
        /* Initialize entities needed in database */
        String userName = "Test";
        String url = "/shopping-cart/flower";
        ShoppingCart shoppingCart =
                initializeShoppingCartEntity("Test product", 1, false);
        Account account = createAccount(userName, "GetShoppingCartTest", shoppingCart);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                delete(url).param("id", shoppingCart.getFlowerOrderList().get(0).getId().toString()).with(user(userName));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        MessageResponseDTO messageResponseDTO =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);
        if (!messageResponseDTO.getMessage().equals(ShoppingCartMessage.PRODUCT_REMOVED_SUCCESSFULLY.toString())) {
            throw new RuntimeException("Unexpected response value");
        }

        /* Retrieve shopping cart again from the database, to check if the new product was added */
        ShoppingCart afterTestResults = repository.getOne(shoppingCart.getId());
        Assertions.assertThat(afterTestResults.getFlowerOrderList().isEmpty()).isTrue();
    }

    @Test
    void removeOccasionalArticleOrderFromShoppingCartTest() throws Exception {
        /* Set testing parameters */
        String userName = "Test";
        String url = "/shopping-cart/occasional-article";
        MonetaryAmount money = Money.of(6.99, Monetary.getCurrency(env.getProperty("app-monetary-currency")));

        /* Initialize entities needed in database */
        OccasionalArticle testingProduct =
                new OccasionalArticle("Testing Product", money, "Testing Product", "Testing Product");
        occasionalArticleRepository.saveAndFlush(testingProduct);
        OccasionalArticleOrder occasionalArticleOrder = new OccasionalArticleOrder(1, testingProduct);
        ShoppingCart shoppingCart =
                initializeShoppingCartEntity("Test product", 0, false);
        shoppingCart.setOccasionalArticleOrderList(new LinkedList<>());
        shoppingCart.getOccasionalArticleOrderList().add(occasionalArticleOrder);
        Account account = createAccount(userName, "GetShoppingCartTest", shoppingCart);


        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                delete(url).param("id", shoppingCart.getOccasionalArticleOrderList().get(0).getId().toString())
                        .with(user(userName));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        MessageResponseDTO messageResponseDTO =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);
        if (!messageResponseDTO.getMessage().equals(ShoppingCartMessage.PRODUCT_REMOVED_SUCCESSFULLY.toString())) {
            throw new RuntimeException("Unexpected response value");
        }

        /* Retrieve shopping cart again from the database, to check if the new product was added */
        ShoppingCart afterTestResults = repository.getOne(shoppingCart.getId());
        Assertions.assertThat(afterTestResults.getFlowerOrderList().isEmpty()).isTrue();
    }

    @Test
    void removeSouvenirOrderFromShoppingCartTest() throws Exception {
        /* Set testing parameters */
        String userName = "Test";
        String url = "/shopping-cart/occasional-article";
        MonetaryAmount money = Money.of(6.99, Monetary.getCurrency(env.getProperty("app-monetary-currency")));

        /* Initialize entities needed in database */
        Souvenir testingProduct = new Souvenir("Testing Product", money, "Testing Product");
        souvenirRepository.saveAndFlush(testingProduct);
        SouvenirOrder souvenirOrder = new SouvenirOrder(1, testingProduct);
        ShoppingCart shoppingCart =
                initializeShoppingCartEntity("Test product", 0, false);
        shoppingCart.setSouvenirOrderList(new LinkedList<>());
        shoppingCart.getSouvenirOrderList().add(souvenirOrder);
        Account account = createAccount(userName, "GetShoppingCartTest", shoppingCart);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                delete(url).param("id", shoppingCart.getSouvenirOrderList().get(0).getId().toString())
                        .with(user(userName));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        MessageResponseDTO messageResponseDTO =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);
        if (!messageResponseDTO.getMessage().equals(ShoppingCartMessage.PRODUCT_REMOVED_SUCCESSFULLY.toString())) {
            throw new RuntimeException("Unexpected response value");
        }

        /* Retrieve shopping cart again from the database, to check if the new product was added */
        ShoppingCart afterTestResults = repository.getOne(shoppingCart.getId());
        Assertions.assertThat(afterTestResults.getFlowerOrderList().isEmpty()).isTrue();
    }

    /**
     * Delete all entities related to repositories used in this test to prevent accidental errors, and violations
     */
    private void purgeDatabase() {
        accountRepository.deleteAll();
        repository.deleteAll();
        bouquetRepository.deleteAll();
        flowerOrderRepository.deleteAll();
        flowerRepository.deleteAll();
        occasionalArticleRepository.deleteAll();
        souvenirRepository.deleteAll();

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
                null,
                null,
                initializeFlowerOrders(productNamePrefix, numberOfProducts),
                null);

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

}
