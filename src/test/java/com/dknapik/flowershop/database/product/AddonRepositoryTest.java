package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.Addon;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import javax.money.Monetary;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AddonRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private Environment env;
    @Autowired
    private AddonRepository addonRepository;

    @Test
    public void saveToDatabaseTest() {
        String name = "Test Addon";
        String colour = "Blue";
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
    public void findByNameTest() {
        String name = "Test1";
        Money price = Money.of(2, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Addon[] addonArray = {
                new Addon(name, "Blue", price, "TestDescription"),
                new Addon(name, "Red", price, "TestDescription"),
                new Addon(name, "Yellow", price, "TestDescription"),
                new Addon("Test2", "Blue", price, "TestDescription"),
                new Addon("Test3", "Green", price, "TestDescription"),
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
    public void findByNameAndColourTest() {
        String name = "Test1";
        String colour = "Blue";
        Money price = Money.of(2, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Addon[] addonArray = {
                new Addon(name, colour, price, "Test Description"),
                new Addon(name, colour, price, "Test Description"),
                new Addon(name, "Red", price, "Test Description"),
                new Addon("Test2", colour, price, "Test Description"),
                new Addon("Test3", "Yellow", price, "Test Description"),
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
