package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.model.productorder.OccasionalArticleOrder;
import com.dknapik.flowershop.model.product.OccasionalArticle;
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
final class OccasionalArticleOrderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private OccasionalArticleRepository occasionalArticleRepository;
    @Autowired
    private OccasionalArticleOrderRepository orderRepository;
    @Autowired
    private Environment env;


    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    void saveToDatabaseTest() {
        /* Retrieve OccasionalArticle that will be part of the new entity from database */
        Money price = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        OccasionalArticle occasionalArticle =
                retrieveOccasionalArticle("Occasional Article",
                        price,
                        "Example description",
                        "Birthday");


        /* Create new article order and save it to database */
        OccasionalArticleOrder occasionalArticleOrder =
                new OccasionalArticleOrder(5, occasionalArticle);
        orderRepository.saveAndFlush(occasionalArticleOrder);


        /* Retrieve previously saved article order and chcek if it is the same  */
        OccasionalArticleOrder searchResult =
                entityManager.find(OccasionalArticleOrder.class, occasionalArticleOrder.getId());
        Assertions.assertThat(searchResult).isEqualToComparingFieldByField(occasionalArticleOrder);
    }


    /**
     * Check if product entities are eagerly extracted from order entity
     *
     * @throws NoSuchElementException
     */
    @Test
    void fetchOccasionalArticleTest() throws NoSuchElementException {
        /* Retrieve OccasionalArticle that will be part of the new entity from database */
        Money price = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        OccasionalArticle occasionalArticle =
                retrieveOccasionalArticle("Occasional Article",
                        price,
                        "Example description",
                        "Birthday");

        /* Create new article order and save it to database */
        OccasionalArticleOrder occasionalArticleOrder =
                new OccasionalArticleOrder(5, occasionalArticle);
        orderRepository.saveAndFlush(occasionalArticleOrder);


        /* Retrieve order entity from database and check if OccasionalArticle was retrieved also (Eager Fetch) */
        OccasionalArticleOrder searchResult = orderRepository.getOne(occasionalArticleOrder.getId());
        Assertions.assertThat(occasionalArticle).isEqualTo(searchResult.getOccasionalArticle());
    }

    /**
     * Check if OccasionalArticle with provided parameters exists already in database and create new entity if not.
     *
     * @return OccasionalArticle entity from database
     */
    private OccasionalArticle retrieveOccasionalArticle(String name, Money price, String description, String theme) {
        Optional<OccasionalArticle> occasionalArticle =
                occasionalArticleRepository.findByNameAndDescriptionAndTheme(name, description, theme);

        /* Check if article already exists in database and create it and save if false */
        if (!occasionalArticle.isPresent()) {
            occasionalArticle = Optional.of(new OccasionalArticle(name, price, description, theme));
            entityManager.persist(occasionalArticle.get());
            entityManager.flush();
        }

        return occasionalArticle.get();
    }
}
