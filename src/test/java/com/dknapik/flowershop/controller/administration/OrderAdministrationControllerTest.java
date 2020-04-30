package com.dknapik.flowershop.controller.administration;

import com.dknapik.flowershop.constants.OrderMessage;
import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.constants.sorting.OrderSortingProperty;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.order.OrderDTO;
import com.dknapik.flowershop.mapper.order.OrderMapper;
import com.dknapik.flowershop.mapper.order.ShoppingCartMapper;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.AccountRole;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.OrderStatus;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.model.productorder.FlowerOrder;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(value = "build/generated-snippets/order")
@TestPropertySource(properties = {"app-monetary-currency=PLN", "app-debug-mode=false"})
@Transactional
public class OrderAdministrationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Environment env;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FlowerRepository flowerRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @BeforeEach
    void cleanUpBefore() {
        purgeDatabase();
    }

    @AfterEach
    void cleanUpAfter() {
        // purgeDatabase();
    }

    @Test
    void updateOrderTest() throws Exception {
        String url = "/order-administration";
        MonetaryAmount money = Money.of(6.99, Monetary.getCurrency(env.getProperty("app-monetary-currency")));

        /* Create test related values and entities */
        Flower product = initializeFlowerEntity("TestingFlower", money);
        Order expectedResult = populateDatabaseWithOrderEntities(1).get(0);
        expectedResult.setShoppingCart(initializeShoppingCartEntity("Testing Order Shopping Cart",
                false, product));
        expectedResult.setMessage("Updated Message");
        expectedResult.setStatus(OrderStatus.ASSIGNED);
        Account account = createAccount("Order Testing User", "Order Testing User",
                null, AccountRole.ROLE_EMPLOYEE);
        OrderDTO orderToUpdate = orderMapper.mapToDTO(expectedResult);

        /* Deserialize required entity */
        String value = objectMapper.writeValueAsString(orderToUpdate);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                put(url).content(value).contentType(MediaType.APPLICATION_JSON)
                        .with(user(account.getName()).authorities(
                                new SimpleGrantedAuthority(account.getRole().toString())));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        MessageResponseDTO resultDTO =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);

        /* Check if Response matches */
        if (!resultDTO.getMessage().contentEquals(OrderMessage.ORDER_UPDATED_SUCCESSFULLY.toString())) {
            throw new RuntimeException("Response message doesn't match expected one");
        }

        /* Check if results match */
        Order afterUpdate = orderRepository.findById(expectedResult.getId()).orElseThrow(() ->
                new RuntimeException("Order entity not found in database"));
        Assertions.assertThat(afterUpdate).isEqualToComparingFieldByField(expectedResult);
    }

    @Test
    void retrieveOrdersPage() throws Exception {
        String url = "/order-administration/page";
        MonetaryAmount money = Money.of(6.99, Monetary.getCurrency(env.getProperty("app-monetary-currency")));


        /* Initialize required entities */
        Flower product = initializeFlowerEntity("TestingFlower", money);
        Account account =
                createAccount("Order Testing User", "Order Testing User",
                        initializeShoppingCartEntity("test", false, product), AccountRole.ROLE_EMPLOYEE);
        List<Order> entityList = populateDatabaseWithOrderEntities(ProductProperties.PAGE_SIZE);
        List<OrderDTO> controlValuesList = new LinkedList<>();
        for (Order order : entityList) {
            controlValuesList.add(orderMapper.mapToDTO(order));
        }

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder = get(url).param("page", "0")
                .param("elements", String.valueOf(ProductProperties.PAGE_SIZE))
                .param("sorting", OrderSortingProperty.NONE.toString())
                .with(user(account.getName()).authorities(new SimpleGrantedAuthority(account.getRole().toString())));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        TypeReference<RestResponsePage<OrderDTO>> typeReference =
                new TypeReference<RestResponsePage<OrderDTO>>() {
                };
        RestResponsePage<OrderDTO> resultValue =
                objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        /* Check if results match */
        Assertions.assertThat(resultValue.getContent()).containsSequence(controlValuesList);
    }

    /**
     * Create provided number of entities for test
     *
     * @param numberOfEntities - how many entities to create
     * @return List with Orders entities.
     */
    private List<Order> populateDatabaseWithOrderEntities(int numberOfEntities) {

        List<Order> entitiesList = new LinkedList<>();
        for (int i = 0; i < numberOfEntities; i++) {
            entitiesList.add(new Order());
        }

        orderRepository.saveAll(entitiesList);
        orderRepository.flush();
        return entitiesList;
    }

    /**
     * Creates, persists and returns Account entity.
     *
     * @param userName     - account name
     * @param password     - account password
     * @param shoppingCart - shopping cart that should be saved together with account.
     * @return Persisted Account entity.
     */
    private Account createAccount(String userName, String password, ShoppingCart shoppingCart, AccountRole accountRole) {
        /* Create new account entity from provided arguments */
        Account newAccount = new Account(userName, userName + "@test.com", password, accountRole);
        newAccount.setShoppingCart(shoppingCart);

        /* Persist created entity and return it */
        accountRepository.saveAndFlush(newAccount);
        return newAccount;
    }

    /**
     * Create and return Shopping Cart entity.
     *
     * @param name           - name of the shopping cart to create.
     * @param saveToDatabase - if shopping cart should be saved through repository to database.
     * @return - ShoppingCart Entity
     */
    private ShoppingCart initializeShoppingCartEntity(String name, boolean saveToDatabase, Flower product) {
        /* Create Shopping Cart entity from provided arguments */
        ShoppingCart shoppingCartEntity = new ShoppingCart(name, null, null,
                new LinkedList<FlowerOrder>(), null);
        shoppingCartEntity.getFlowerOrderList().add(new FlowerOrder(1, product));

        /* Save created entity to database */
        if (saveToDatabase) {
            shoppingCartRepository.saveAndFlush(shoppingCartEntity);
        }
        return shoppingCartEntity;
    }

    private Flower initializeFlowerEntity(String flowerName, MonetaryAmount price) {
        Flower flower = new Flower(flowerName, price, flowerName, 5);
        this.flowerRepository.saveAndFlush(flower);
        return flower;
    }

    /**
     * Delete all entities related to repositories used in this test to prevent accidental errors, and violations
     */
    private void purgeDatabase() {
        accountRepository.deleteAll();
        orderRepository.deleteAll();
        shoppingCartRepository.deleteAll();
    }
}
