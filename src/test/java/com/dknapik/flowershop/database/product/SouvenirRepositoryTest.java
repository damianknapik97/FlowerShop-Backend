package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.Souvenir;
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
import java.util.NoSuchElementException;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class SouvenirRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private SouvenirRepository souvenirRepository;
    @Autowired
    private Environment env;


    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    public void saveToDatabaseTest() {
        /* Create new Souvenir object */
        String souvenirName = "Save Souvenir Test";
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Example description";
        Souvenir souvenir = new Souvenir(souvenirName, money, description);

        /* Save it using repository */
        souvenirRepository.saveAndFlush(souvenir);

        /* Retrieve it using entity manager, confirm that it is the same object */
        Souvenir searchResult = entityManager.find(Souvenir.class, souvenir.getId());
        Assertions.assertThat(searchResult).isEqualToComparingFieldByField(souvenir);
    }


    @Test
    public void findByNameTest() throws NoSuchElementException {
        /* Create Souvenir object */
        String souvenirName = "Test Souvenir";
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Example description";
        Souvenir souvenir = new Souvenir(souvenirName, money, description);

        /* Save it to database using entity manager */
        entityManager.persist(souvenir);
        entityManager.flush();

        /* Retrieve it using repository with find by name method and compare the two objects */
        Souvenir searchResult = souvenirRepository.findByName(souvenirName).get();
        Assertions.assertThat(searchResult).isEqualTo(searchResult);
    }
}
