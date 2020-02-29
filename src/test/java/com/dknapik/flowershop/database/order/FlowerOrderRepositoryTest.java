package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.model.productorder.FlowerOrder;
import com.dknapik.flowershop.model.product.Flower;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
final class FlowerOrderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private FlowerOrderRepository orderRepository;
    @Autowired
    private FlowerRepository flowerRepository;
    @Autowired
    private Environment env;


    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    void saveToDatabaseTest() {
        /* Create order entity using product entity */
        Flower product = createFlowerEntity();
        FlowerOrder order = new FlowerOrder(5, product);

        /* Save to database */
        orderRepository.saveAndFlush(order);

        /* Retrieve entity and check if field match the one that was saved */
        FlowerOrder retrievedEntity = entityManager.find(FlowerOrder.class, order.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if entity can be retrieved from database using JPA repository.
     */
    @Test
    void retrieveFromDatabaseTest() {
        /* Create order entity using product entity */
        Flower product = createFlowerEntity();
        FlowerOrder order = new FlowerOrder(5, product);

        /* Save order to database */
        entityManager.persistAndFlush(order);

        Optional<FlowerOrder> retrievedEntity = orderRepository.findById(order.getId());
        Assertions.assertThat(retrievedEntity.get()).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if product entities are eagerly extracted from order entity
     */
    @Test
    void fetchProductTest() throws NoSuchElementException {
        /* Create order entity using product entity */
        Flower product = createFlowerEntity();
        FlowerOrder order = new FlowerOrder(5, product);

        /* Save order to database */
        entityManager.persistAndFlush(order);


        /* Retrieve product entity from database and check if it matches the one that was saved in order*/
        Optional<Flower> searchResult = flowerRepository.findById(product.getId());
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(product);
    }

    private Flower createFlowerEntity() {
        Money price = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Flower newFlower = new Flower("Testing Flower", price, "Test Description", 5);
        flowerRepository.saveAndFlush(newFlower);
        return flowerRepository.findById(newFlower.getId()).get();
    }
}
