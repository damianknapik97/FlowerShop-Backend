package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.AddonOrder;
import com.dknapik.flowershop.model.Bouquet;
import com.dknapik.flowershop.model.FlowerOrder;
import com.dknapik.flowershop.model.product.Addon;
import com.dknapik.flowershop.model.product.Flower;
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
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class BouquetRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BouquetRepository bouquetRepository;
    @Autowired
    private Environment env;

    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    public void saveToDatabaseTest() {
        /* Create order entity using product entity */
        Bouquet bouquet = new Bouquet();
        bouquetRepository.saveAndFlush(bouquet);

        /* Save to database using repository */
        bouquetRepository.saveAndFlush(bouquet);

        /* Retrieve entity and check if field match the one that was saved */
        Bouquet retrievedEntity = entityManager.find(Bouquet.class, bouquet.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(bouquet);
    }

    /**
     * Check if entity can be retrieved from database using JPA repository.
     */
    @Test
    public void retrieveFromDatabaseTest() {
        /* Create order entity using product entity */
        Bouquet bouquet = new Bouquet();
        bouquetRepository.saveAndFlush(bouquet);

        /* Save entity to database using test entity manager */
        entityManager.persistAndFlush(bouquet);

        /* Retrieve entity using repository and check if field match the one that was saved */
        Optional<Bouquet> retrievedEntity = bouquetRepository.findById(bouquet.getId());
        Assertions.assertThat(retrievedEntity.get()).isEqualToComparingFieldByField(bouquet);
    }

    @Test
    public void deleteTest() {
        /* Initialize entities that will be part of this entity */
        List<FlowerOrder> flowerOrderList =
                createFlowerOrderEntities(createFlowerEntities("Test Flower", 10), 5);
        List<AddonOrder> addonOrderList =
                createAddonOrderEntities(createAddonEntities("Test Addon", 2));
        Money price = Money.of(3.25, Monetary.getCurrency(env.getProperty("app-monetary-currency")));

        /* Create new bouquet entity */
        Bouquet bouquet =
                new Bouquet("My Testing Bouquet", price, 5, flowerOrderList, addonOrderList, false);

        /* Save new bouquet entity together with associated entities  */
        bouquetRepository.saveAndFlush(bouquet);

        /* Delete newly saved entity, and check if sub entities were also cascade deleted */
        bouquetRepository.deleteById(bouquet.getId());

        /* Search for associated entities and add them if they are retrievable (not null) */
        List<FlowerOrder> controlGroup = new LinkedList<>();
        FlowerOrder retrievedEntity;
        for (FlowerOrder flowerOrder : flowerOrderList) {
            retrievedEntity = entityManager.find(FlowerOrder.class, flowerOrder.getId());
            if (retrievedEntity != null ){
                controlGroup.add(retrievedEntity);
            }
        }

        /* Check if any of the entities that should be deleted, were retrieved from database */
        Assertions.assertThat(controlGroup.isEmpty()).isTrue();
    }

    /**
     *  Create FlowerOrder entities, WITHOUT saving them to database.
     *
     * @param flowers - list with flowers on which Order entities will be created
     * @param itemCount - how many products in one order
     * @return - List with database unsaved Flower Orders
     */
    private List<FlowerOrder> createFlowerOrderEntities(@NotNull List<Flower> flowers, int itemCount) {
        Objects.requireNonNull(flowers, "Flower list must be initialized!");
        List<FlowerOrder> flowerOrderList = new LinkedList<>();

        for (Flower flower : flowers) {
            flowerOrderList.add(new FlowerOrder(itemCount, flower));
        }

        return flowerOrderList;
    }

    /**
     * Create AddonOrder entities WITHOUT saving them to database.
     *
     * @param addons - list with addons on which Order entities will be created
     * @return - List with database unsaved Addon Orders
     */
    private List<AddonOrder> createAddonOrderEntities(@NotNull List<Addon> addons) {
        Objects.requireNonNull(addons, "Addon list must be initialized!");
        List<AddonOrder> addonOrderList = new LinkedList<>();

        for (Addon addon : addons) {
            addonOrderList.add(new AddonOrder(addon));
        }

        return addonOrderList;
    }

    /**
     * Create multiple Flower entities and save them into database
     *
     * @param name - prefix to the full name which is created from combination of name String and entity number
     * @param numberOfEntities - how many entities should be created
     * @return - list with created entities
     */
    private List<Flower> createFlowerEntities(@NotNull String name, int numberOfEntities) {
        Objects.requireNonNull(name, "Flower list must be initialized !");
        Money price = Money.of(5.55, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        List<Flower> flowerEntityList = new LinkedList<>();

        for (int i = 0; i < numberOfEntities; i++) {
            flowerEntityList.add(new Flower(name + i, price, "Exemplary description", 5));
            entityManager.persist(flowerEntityList.get(i));
        }
        entityManager.flush();

        return flowerEntityList;
    }

    /**
     * Create multiple Addon entities and save them to database
     *
     * @param name - prefix to the full name which is created from combination of name String and entity number
     * @param numberOfEntities - how many entities should be created
     * @return - list with created entities
     */
    private List<Addon> createAddonEntities(@NotNull String name, int numberOfEntities) {
        Objects.requireNonNull(name, "Name can't be null !");
        Money price = Money.of(3.25, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        List<Addon> addonEntityList = new LinkedList<>();

        for (int i = 0; i < numberOfEntities; i++) {
            addonEntityList.add(new Addon(name + i, "Blue", price, "Exemplary description"));
            entityManager.persist(addonEntityList.get(i));
        }
        entityManager.flush();

        return addonEntityList;
    }



}
