package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.model.order.*;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.money.Monetary;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
final class OrderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private Environment env;


    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    void saveToDatabaseTest() {
        /* Create entity */
        Order order = createOrderEntity();

        /* Save entity to database using repository */
        orderRepository.saveAndFlush(order);

        /* Retrieve entity from database and compare it to the one saved */
        Order retrievedEntity = entityManager.find(Order.class, order.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if entity can be extracted from database using repository.
     */
    @Test
    void retrieveFromDatabase() {
        /* Create entity */
        Order order = createOrderEntity();

        /* Save entity to database */
        entityManager.persistAndFlush(order);

        /* Retrieve entity using repository and check if it is the same as the one saved */
        Optional<Order> searchResult = orderRepository.findById(order.getId());
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(order);
    }

    @Test
    void cascadeDeletePayment() {
        /* Create entity */
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Payment payment = new Payment(money, PaymentType.BLIK);
        Order order = createOrderEntity();
        order.setPayment(payment);

        /* Save entity to database */
        entityManager.persistAndFlush(order);

        /* Delete order entity */
        orderRepository.deleteById(order.getId());

        /* Retrieve entity and check if it still exists in database */
        Optional<Payment> searchResult = paymentRepository.findById(payment.getId());
        Assertions.assertThat(searchResult.isPresent()).isFalse();
    }

    @Test
    void cascadeDeleteDeliveryAddress() {
        /* Create entity */
        DeliveryAddress deliveryAddress =
                new DeliveryAddress("Wroclaw", "42-468", "Test Street", "543");
        Order order = createOrderEntity();
        order.setDeliveryAddress(deliveryAddress);

        /* Save entity to database */
        entityManager.persistAndFlush(order);

        /* Delete order entity */
        orderRepository.deleteById(order.getId());

        /* Retrieve entity and check if it still exists in database */
        Optional<DeliveryAddress> searchResult = deliveryAddressRepository.findById(deliveryAddress.getId());
        Assertions.assertThat(searchResult.isPresent()).isFalse();
    }

    @Test
    void cascadeDeleteShoppingCart() {
        /* Create entity */
        ShoppingCart shoppingCart = new ShoppingCart("Test Shopping Cart");
        Order order = createOrderEntity();
        order.setShoppingCart(shoppingCart);

        /* Save entity to database */
        entityManager.persistAndFlush(order);

        /* Delete order entity */
        orderRepository.deleteById(order.getId());

        /* Retrieve entity and check if it still exists in database */
        Optional<ShoppingCart> searchResult = shoppingCartRepository.findById(shoppingCart.getId());
        Assertions.assertThat(searchResult.isPresent()).isFalse();
    }

    @Test
    void fetchPayment() {
        /* Create entity */
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Payment payment = new Payment(money, PaymentType.BLIK);
        Order order = createOrderEntity();
        order.setPayment(payment);

        /* Save entity to database */
        entityManager.persistAndFlush(order);


        /* Retrieve entity and check if it is accessible */
        Optional<Payment> searchResult = paymentRepository.findById(payment.getId());
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(payment);
    }

    @Test
    void fetchDeliveryAddress() {
        /* Create entity */
        DeliveryAddress deliveryAddress =
                new DeliveryAddress("Wroclaw", "42-468", "Test Street", "543");
        Order order = createOrderEntity();
        order.setDeliveryAddress(deliveryAddress);

        /* Save entity to database */
        entityManager.persistAndFlush(order);

        /* Retrieve entity and check if it is accessible */
        Optional<DeliveryAddress> searchResult = deliveryAddressRepository.findById(deliveryAddress.getId());
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(deliveryAddress);
    }

    @Test
    void fetchShoppingCart() {
        /* Create entity */
        ShoppingCart shoppingCart = new ShoppingCart("Test Shopping Cart");
        Order order = createOrderEntity();
        order.setShoppingCart(shoppingCart);

        /* Save entity to database */
        entityManager.persistAndFlush(order);

        /* Retrieve entity and check if it is accessible */
        Optional<ShoppingCart> searchResult = shoppingCartRepository.findById(shoppingCart.getId());
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(shoppingCart);
    }

    private Order createOrderEntity() {
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Payment payment = new Payment(money, PaymentType.BANK_TRANSFER);
        DeliveryAddress deliveryAddress =
                new DeliveryAddress("Katowice", "42-500", "Test Street", "328");
        ShoppingCart shoppingCart = new ShoppingCart("New Shopping Cart");

        return new Order("Happy New Year!", payment, deliveryAddress, shoppingCart);
    }
}
