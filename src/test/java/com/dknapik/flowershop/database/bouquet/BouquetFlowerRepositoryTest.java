package com.dknapik.flowershop.database.bouquet;

import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.model.bouquet.BouquetFlower;
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
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class BouquetFlowerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BouquetFlowerRepository orderRepository;
    @Autowired
    private FlowerRepository flowerRepository;
    @Autowired
    private Environment env;

    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    public void saveToDatabaseTest() {
        /* Create bouquet flower using product entity */
        Flower product = createFlowerEntity();
        BouquetFlower bouquetFlower = new BouquetFlower(5, product);

        /* Save to database using repository */
        orderRepository.saveAndFlush(bouquetFlower);

        /* Retrieve entity and check if field match the one that was saved */
        BouquetFlower retrievedEntity = entityManager.find(BouquetFlower.class, bouquetFlower.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(bouquetFlower);
    }

    /**
     * Check if entity can be retrieved from database using JPA repository.
     */
    @Test
    public void retrieveFromDatabaseTest() {
        /* Create bouquet flower using product entity */
        Flower product = createFlowerEntity();
        BouquetFlower bouquetFlower = new BouquetFlower(5, product);

        /* Save order to database using test entity manager */
        entityManager.persistAndFlush(bouquetFlower);

        /* Retrieve entity and check if field match the one that was saved */
        Optional<BouquetFlower> retrievedEntity = orderRepository.findById(bouquetFlower.getId());
        Assertions.assertThat(retrievedEntity.get()).isEqualToComparingFieldByField(bouquetFlower);
    }

    private Flower createFlowerEntity() {
        Money price = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Flower newFlower = new Flower("Testing Flower", price, "Test Description", 5);
        flowerRepository.saveAndFlush(newFlower);
        return flowerRepository.findById(newFlower.getId()).get();
    }
}