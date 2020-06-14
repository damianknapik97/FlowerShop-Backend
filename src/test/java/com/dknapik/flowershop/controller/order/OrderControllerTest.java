package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.mapper.order.OrderMapper;
import com.dknapik.flowershop.mapper.order.ShoppingCartMapper;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.AccountRole;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.model.productorder.FlowerOrder;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(value = "build/generated-snippets/order")
@TestPropertySource(properties = {"app-monetary-currency=PLN", "app-debug-mode=false"})
@Transactional
class OrderControllerTest {
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
    void createOrderFromCurrentShoppingCart() throws Exception {
        String url = "/order";
        MonetaryAmount money = Money.of(6.99, Monetary.getCurrency(env.getProperty("app-monetary-currency")));

        /* Initialize required entities */
        Flower product = initializeFlowerEntity("TestingFlower", money);
        ShoppingCart shoppingCart =
                initializeShoppingCartEntity("Testing Shopping Cart", false, product);
        Account account = createAccount("Order Testing User", "Order Testing User",
                shoppingCart, AccountRole.ROLE_USER);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                post(url).with(user(account.getName()));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        MessageResponseDTO resultID = objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);


        Assertions.assertThat(resultID.getMessage().isEmpty()).isFalse();
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
