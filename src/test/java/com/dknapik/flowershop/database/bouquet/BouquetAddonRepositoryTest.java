package com.dknapik.flowershop.database.bouquet;

import com.dknapik.flowershop.database.product.AddonRepository;
import com.dknapik.flowershop.model.bouquet.BouquetAddon;
import com.dknapik.flowershop.model.product.Addon;
import com.dknapik.flowershop.model.product.AddonColour;
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
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class BouquetAddonRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AddonRepository productRepository;
    @Autowired
    private BouquetAddonRepository orderRepository;
    @Autowired
    private Environment env;

    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    public void saveToDatabaseTest() {
        /* Create order entity using product entity */
        Addon product = createAddonEntity();
        BouquetAddon order = new BouquetAddon(product);

        /* Save to database using repository */
        orderRepository.saveAndFlush(order);

        /* Retrieve entity and check if field match the one that was saved */
        BouquetAddon retrievedEntity = entityManager.find(BouquetAddon.class, order.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if entity can be retrieved from database using JPA repository.
     */
    @Test
    public void retrieveFromDatabaseTest() {
        Addon product = createAddonEntity();
        BouquetAddon order = new BouquetAddon(product);

        /* Save order to database */
        entityManager.persistAndFlush(order);

        /* Retrieve entity using repository and check if field match the one that was saved */
        Optional<BouquetAddon> retrievedEntity = orderRepository.findById(order.getId());
        Assertions.assertThat(retrievedEntity.get()).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if product entities are eagerly extracted from order entity
     */
    @Test
    public void fetchProductTest() throws NoSuchElementException {
        Addon product = createAddonEntity();
        BouquetAddon order = new BouquetAddon(product);

        /* Save order to database */
        entityManager.persistAndFlush(order);

        /* Retrieve product entity from database and check if it matches the one that was saved in order*/
        Optional<Addon> searchResult = productRepository.findById(product.getId());
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(product);
    }

    /**
     * Check if no errors will be invoked if multiple BouquetAddon instance will be created and saved using the same
     * product entity.
     */
    @Test
    public void saveMultipleEntitiesUsingOneProduct() {
        /* Create multiple entities */
        Addon product = createAddonEntity();
        BouquetAddon[] orderArray = {
                new BouquetAddon(product),
                new BouquetAddon(product),
                new BouquetAddon(product),
        };

        /* Save multiple entities to database */
        orderRepository.saveAll(Arrays.asList(orderArray));
        orderRepository.flush();

        /* Retrieve all entities */
        BouquetAddon[] retrievedEntitiesArray = new BouquetAddon[orderArray.length];
        Optional<BouquetAddon> retrivedEntity;
        for (int i = 0; i < orderArray.length; i++) {
            retrivedEntity = orderRepository.findById(orderArray[i].getId());
            retrievedEntitiesArray[i] = retrivedEntity.get();
        }

        Assertions.assertThat(Arrays.asList(retrievedEntitiesArray))
                .containsExactlyInAnyOrderElementsOf(Arrays.asList(orderArray));
    }

    /**
     * Create, save to database and retrieve from database product entity
     */
    private Addon createAddonEntity() {
        Money price = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Addon newAddon =
                new Addon("Test Addon Entity", AddonColour.BLUE, price, "Example addon description");
        productRepository.saveAndFlush(newAddon);
        return productRepository.findById(newAddon.getId()).get();
    }


}
