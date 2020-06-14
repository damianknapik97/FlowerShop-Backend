package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.PaymentMessage;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.order.PaymentDTO;
import com.dknapik.flowershop.mapper.order.PaymentMapper;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.AccountRole;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.Payment;
import com.dknapik.flowershop.model.order.PaymentType;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.services.order.DeliveryAddressService;
import com.dknapik.flowershop.utils.MoneyUtils;
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
@AutoConfigureRestDocs(value = "build/generated-snippets/delivery-address")
@TestPropertySource(properties = {"app-monetary-currency=PLN", "app-debug-mode=false"})
@Transactional
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private DeliveryAddressService deliveryAddressService;
    @Autowired
    private MoneyUtils moneyUtils;

    @BeforeEach
    void beforeEach() {
        purgeDatabase();
    }

    @AfterEach
    void afterEach() {

    }

    @Test
    void createPaymentForOrder() throws Exception {
        String url = "/payment";

        MonetaryAmount deliveryFee = deliveryAddressService.countDeliveryFee();

        /* Initialize required entities */
        Order order = createOrder("Testing Order", new ShoppingCart(), false);
        Account account = createAccountWithOrder("Payment User", "Payment User",
                order, AccountRole.ROLE_USER);
        Payment controlValue = new Payment(deliveryFee, PaymentType.BLIK);
        PaymentDTO dto = paymentMapper.mapToDTO(controlValue);

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
                .contentEquals(PaymentMessage.CREATED_SUCCESSFULLY.toString())) {
            throw new RuntimeException("Response message doesn't match expected one");
        }

        /* Check if results match */
        Order orderAfterUpdate = orderRepository.findById(order.getId()).orElseThrow(() ->
                new RuntimeException("Order entity not found in database"));
        Payment expectedPayment = orderAfterUpdate.getPayment();
        Assertions.assertThat(expectedPayment).isEqualToIgnoringGivenFields(controlValue,
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
