package com.dknapik.flowershop.database.product;

import com.dknapik.flowershop.model.product.OccasionalArticle;
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
public class OccasionalArticleRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private OccasionalArticleRepository occasionalArticleRepository;
    @Autowired
    private Environment env;


    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    public void saveToDatabaseTest() {
        /* Create new entity */
        String occasionalArticleName = "Save Occasional Article Test";
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Example description";
        String theme = "Birthday";
        OccasionalArticle occasionalArticle = new OccasionalArticle(occasionalArticleName, money, description, theme);

        /* Save it using repository */
        occasionalArticleRepository.saveAndFlush(occasionalArticle);

        /* Retrieve it using entity manager, confirm that it is the same object */
        OccasionalArticle searchResult = entityManager.find(OccasionalArticle.class, occasionalArticle.getId());
        Assertions.assertThat(searchResult).isEqualToComparingFieldByField(occasionalArticle);
    }


    @Test
    public void findByNameAndDescriptionAndThemeTest() throws NoSuchElementException {
        /* Create Souvenir object */
        String occasionalArticleName = "Test Occasional Article";
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        String description = "Example description";
        String theme = "Birthday";
        OccasionalArticle occasionalArticle = new OccasionalArticle(occasionalArticleName, money, description, theme);

        /* Save it to database using entity manager */
        entityManager.persist(occasionalArticle);
        entityManager.flush();

        /* Retrieve it using repository with find by name method and compare the two objects */
        OccasionalArticle searchResult = occasionalArticleRepository
                .findByNameAndDescriptionAndTheme(occasionalArticle.getName(),
                        occasionalArticle.getDescription(),
                        occasionalArticle.getTheme())
                .get();

        Assertions.assertThat(searchResult).isEqualTo(searchResult);
    }

}
