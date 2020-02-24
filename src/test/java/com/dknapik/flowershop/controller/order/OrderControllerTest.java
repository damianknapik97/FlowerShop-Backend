package com.dknapik.flowershop.controller.order;

import com.dknapik.flowershop.constants.ProductProperties;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.order.OrderRepository;
import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.order.OrderDTO;
import com.dknapik.flowershop.dto.product.FlowerDTO;
import com.dknapik.flowershop.mapper.order.OrderMapper;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.order.Order;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(value = "build/generated-snippets/order" )
@TestPropertySource(properties = {"app-monetary-currency=PLN", "app-debug-mode=false"})
@Transactional
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void createOrderFromShoppingCartTest() throws Exception {
        String url = "/order";

        /* Initialize required entities */
        ShoppingCart shoppingCart = initializeShoppingCartEntity("Testing Shopping Cart", false);
        Account account = createAccount("Order Testing User", "Order Testing User", shoppingCart);
        OrderDTO expectedResult =  orderMapper.mapToDTO(new Order(shoppingCart));

        /* Deserialize required entity */
        String value = objectMapper.writeValueAsString(shoppingCart);

        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                post(url).content(value).contentType(MediaType.APPLICATION_JSON).with(user(account.getName()));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        /* Map response */
        OrderDTO resultDTO = objectMapper.readValue(result.getResponse().getContentAsString(), OrderDTO.class);

        /* Check if results match */
        Assertions.assertThat(resultDTO).isEqualToComparingFieldByField(expectedResult);
    }

    @Test
    public void updateOrderTest() {

    }

    @Test
    public void retrieveOrdersPage() throws Exception {
        String url = "/order/page";

        /* Initialize required entities */
        Account account =
                createAccount("Order Testing User", "Order Testing User", initializeShoppingCartEntity("test", false));
        List<Order> entityList = populateDatabaseWithOrderEntities(ProductProperties.PAGE_SIZE);
        List<OrderDTO> controlValuesList = new LinkedList<>();
        for (Order order : entityList) {
            controlValuesList.add(orderMapper.mapToDTO(order));
        }


        /* Create Request */
        MockHttpServletRequestBuilder requestBuilder =
                get(url).param("page", "0").with(user(account.getName()));

        /* Perform Request, Check status, Create Documentation */
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
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
        //Assertions.assertThat(resultValue.getContent()).containsExactlyInAnyOrder((OrderDTO) controlValuesList);
    }

    /**
     *
     * @param numberOfEntities
     * @return
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
     * Create and return Shopping Cart entity.
     *
     * @param name - name of the shopping cart to create.
     * @param saveToDatabase    - if shopping cart should be saved through repository to database.
     * @return - ShoppingCart Entity
     */
    private ShoppingCart initializeShoppingCartEntity(String name, boolean saveToDatabase) {
        /* Create Shopping Cart entity from provided arguments */
        ShoppingCart shoppingCartEntity = new ShoppingCart(name);

        /* Save created entity to database */
        if (saveToDatabase) {
            shoppingCartRepository.saveAndFlush(shoppingCartEntity);
        }
        return shoppingCartEntity;
    }

}
