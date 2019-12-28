package com.dknapik.flowershop.database;

import com.dknapik.flowershop.database.product.AddonRepository;
import com.dknapik.flowershop.model.AddonOrder;
import com.dknapik.flowershop.model.product.Addon;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.money.Monetary;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class AddonOrderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AddonRepository productRepository;
    @Autowired
    private AddonOrderRepository orderRepository;
    @Autowired
    private Environment env;

    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    public void saveToDatabaseTest() {
        /* Create order entity using product entity */
        Addon product = createAddonEntity();
        AddonOrder order = new AddonOrder(product);

        /* Save to database */
        orderRepository.saveAndFlush(order);

        /* Retrieve entity and check if field match the one that was saved */
        AddonOrder retrievedEntity = entityManager.find(AddonOrder.class, order.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if entity can be retrieved from database using JPA repository.
     */
    @Test
    public void retrieveFromDatabaseTest() {
        Addon product = createAddonEntity();
        AddonOrder order = new AddonOrder(product);
        /* Save order to database */
        entityManager.persistAndFlush(order);

        Optional<AddonOrder> retrievedEntity = orderRepository.findById(order.getId());
        Assertions.assertThat(retrievedEntity.get()).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if product entities are eagerly extracted from order entity
     */
    @Test
    public void fetchProductTest() throws NoSuchElementException {
        Addon product = createAddonEntity();
        AddonOrder order = new AddonOrder(product);

        /* Save order to database */
        entityManager.persistAndFlush(order);

        /* Retrieve product entity from database and check if it matches the one that was saved in order*/
        Optional<Addon> searchResult = productRepository.findById(product.getId());
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(product);
    }

    /**
     * Check if no errors will be invoked if multiple AddonOrder instance will be created and saved using the same
     * product entity.
     */
    @Test
    public void saveMultipleEntitiesUsingOneProduct() {
        /* Create multiple entities */
        Addon product = createAddonEntity();
        AddonOrder[] orderArray = {
                new AddonOrder(product),
                new AddonOrder(product),
                new AddonOrder(product),
        };

        /* Save multiple entities to database */
        orderRepository.saveAll(Arrays.asList(orderArray));
        orderRepository.flush();

        /* Retrieve all entities */
        AddonOrder[] retrievedEntitiesArray = new AddonOrder[orderArray.length];
        Optional<AddonOrder> retrivedEntity;
        for (int i = 0; i < orderArray.length; i++) {
            retrivedEntity = orderRepository.findById(orderArray[i].getId());
            retrievedEntitiesArray[i] = retrivedEntity.get();
        }

        Assertions.assertThat(Arrays.asList(retrievedEntitiesArray))
                .containsExactlyInAnyOrderElementsOf(Arrays.asList(orderArray));
    }

    private Addon createAddonEntity() {
        Money price = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Addon newAddon =
                new Addon("Test Addon Entity", "Blue", price, "Example addon description");
        productRepository.saveAndFlush(newAddon);
        return productRepository.findById(newAddon.getId()).get();
    }


}
