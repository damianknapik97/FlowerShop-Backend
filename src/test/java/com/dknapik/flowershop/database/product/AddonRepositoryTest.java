package com.dknapik.flowershop.database.product;

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

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
class AddonRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private Environment env;
    @Autowired
    private AddonRepository addonRepository;


    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    void saveToDatabaseTest() {
        String name = "Test Addon";
        AddonColour colour = AddonColour.BLUE;
        Money price = Money.of(2, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Test Description";

        /* Save new object using repository */
        Addon addon = new Addon(name, colour, price, description);
        addonRepository.saveAndFlush(addon);

        /* Check if entity manager is able to retrieve the same object */
        Addon foundAddon = entityManager.find(Addon.class, addon.getId());
        Assertions.assertThat(foundAddon).isEqualToComparingFieldByField(addon);
    }

    @Test
    void findByNameTest() {
        String name = "Test1";
        Money price = Money.of(2, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Addon[] addonArray = {
                new Addon(name, AddonColour.BLUE, price, "TestDescription"),
                new Addon(name, AddonColour.RED, price, "TestDescription"),
                new Addon(name, AddonColour.YELLOW, price, "TestDescription"),
                new Addon("Test2", AddonColour.BLUE, price, "TestDescription"),
                new Addon("Test3", AddonColour.GREEN, price, "TestDescription"),
        };

        /* Create control group that will be used as base for comparison */
        Iterable<Addon> controlGroup = Arrays.asList(
                addonArray[0],
                addonArray[1],
                addonArray[2]
        );

        /* Save all created objects */
        for (Addon addon : addonArray) {
            entityManager.persist(addon);
        }
        entityManager.flush();

        /* Retrieve objects using custom method from repository, and check if results match the control group  */
        Iterable<Addon> iterableQueryResults = addonRepository.findByName(name);
        Assertions.assertThat(iterableQueryResults).containsExactlyInAnyOrderElementsOf(controlGroup);
    }

    @Test
    void findByNameAndColourTest() {
        String name = "Test1";
        AddonColour colour = AddonColour.BLUE;
        Money price = Money.of(2, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Addon[] addonArray = {
                new Addon(name, colour, price, "Test Description"),
                new Addon(name, colour, price, "Test Description1"),
                new Addon(name, AddonColour.RED, price, "Test Description2"),
                new Addon("Test2", colour, price, "Test Description3"),
                new Addon("Test3", AddonColour.YELLOW, price, "Test Description4"),
        };

        /* Create control group that will be used as base for comparison */
        Iterable<Addon> controlGroup = Arrays.asList(
                addonArray[0],
                addonArray[1]
        );

        /* Save all created objects */
        for (Addon addon : addonArray) {
            entityManager.persist(addon);
        }
        entityManager.flush();

        /* Retrieve objects using custom method from repository, and check if results match the control group  */
        Iterable<Addon> iterableQueryResults = addonRepository.findByNameAndColour(name, colour);
        Assertions.assertThat(iterableQueryResults).containsExactlyInAnyOrderElementsOf(controlGroup);
    }
}
