package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.DeliveryAddressMessage;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.order.DeliveryAddressDTO;
import com.dknapik.flowershop.mapper.order.DeliveryAddressMapper;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.AccountRole;
import com.dknapik.flowershop.model.order.DeliveryAddress;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(value = "build/generated-snippets/delivery-address")
@TestPropertySource(properties = {"app-monetary-currency=PLN", "app-debug-mode=false"})
@Transactional
final class DeliveryAddressControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DeliveryAddressMapper deliveryAddressMapper;

    @BeforeEach
    void beforeEach() {
        purgeDatabase();
    }

    @AfterEach
    void afterEach() {
        purgeDatabase();
    }

    @Test
    void createDeliveryAddressForOrder() throws Exception {
        String url = "/delivery-address";

        /* Initialize required entities */
        Order order = createOrder("Testing Order", new ShoppingCart(), false);
        Account account = createAccountWithOrder("Delivery Address User", "Delivery Address User",
                order, AccountRole.ROLE_USER);
        DeliveryAddress controlValue = new DeliveryAddress("Testing City", "40-400",
                "Testing Street", "100");
        DeliveryAddressDTO dto = deliveryAddressMapper.mapToDTO(controlValue);

        /* Deserialize required entity */
        String value = objectMapper.writeValueAsString(dto);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder = post(url).param("id", order.getId().toString())
                .content(value)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(account.getName()));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        MessageResponseDTO resultDTO =
                objectMapper.readValue(result.getResponse().getContentAsString(), MessageResponseDTO.class);

        /* Check if Response matches */
        if (!resultDTO.getMessage()
                .contentEquals(DeliveryAddressMessage.DELIVERY_ADDRESS_ADDED_SUCCESSFULLY.toString())) {
            throw new RuntimeException("Response message doesn't match expected one");
        }

        /* Check if results match */
        Order orderAfterUpdate = orderRepository.findById(order.getId()).orElseThrow(() ->
                new RuntimeException("Order entity not found in database"));
        DeliveryAddress expectedDeliveryAddress = orderAfterUpdate.getDeliveryAddress();
        Assertions.assertThat(expectedDeliveryAddress).isEqualToIgnoringGivenFields(controlValue,
                "id");
    }

    /**
     * Creates, persists and returns Account entity.
     *
     * @param userName - account name
     * @param password - account password
     * @param order    - order that should be saved together with account.
     * @return Persisted Account entity.
     */
    private Account createAccountWithOrder(String userName, String password, Order order, AccountRole accountRole) {
        /* Create new account entity from provided arguments */
        Account newAccount = new Account(userName, userName + "@test.com", password, accountRole);
        newAccount.setOrderList(new LinkedList<>());
        newAccount.getOrderList().add(order);


        /* Persist created entity and return it */
        accountRepository.saveAndFlush(newAccount);
        return newAccount;
    }

    /**
     * Create order entity from provided arguments
     *
     * @param message        - message that should order entity contain
     * @param shoppingCart   - shopping cart that should be inside order entity
     * @param saveToDatabase - if order entity should be saved to database
     * @return - Order Entity
     */
    private Order createOrder(String message, ShoppingCart shoppingCart, boolean saveToDatabase) {
        Order order = new Order(shoppingCart);
        order.setMessage(message);

        if (saveToDatabase) {
            orderRepository.save(order);
        }

        return order;
    }

    /**
     * Delete all entities related to repositories used in this test to prevent accidental errors, and violations
     */
    private void purgeDatabase() {
        accountRepository.deleteAll();
        orderRepository.deleteAll();
    }
}
