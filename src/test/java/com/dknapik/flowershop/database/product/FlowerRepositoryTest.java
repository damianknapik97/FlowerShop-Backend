package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.Flower;
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
import java.util.NoSuchElementException;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FlowerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private FlowerRepository flowerRepository;
    @Autowired
    private Environment env;

    @Test
    public void saveToDatabaseTest() {
        /* Create new Flower object */
        String flowerName = "Save Flower Test";
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Example description";
        int height = 10;
        Flower flower = new Flower(flowerName, money, description, height);

        /* Save it using repository */
        flowerRepository.saveAndFlush(flower);

        /* Retrieve it using entity manager, confirm that it is the same object */
        Flower searchResult = entityManager.find(Flower.class, flower.getId());
        Assertions.assertThat(searchResult).isEqualToComparingFieldByField(flower);
    }


    @Test
    public void findByNameTest() throws NoSuchElementException {
        /* Create Flower object */
        String flowerName = "Test Flower";
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Example description";
        int height = 5;
        Flower flower = new Flower(flowerName, money, description, height);

        /* Save it to database using entity manager */
        entityManager.persist(flower);
        entityManager.flush();

        /* Retrieve it using repository with find by name method and compare the two objects */
        Flower searchResult = flowerRepository.findByName("Test Flower").get();
        Assertions.assertThat(searchResult).isEqualTo(searchResult);
    }
}