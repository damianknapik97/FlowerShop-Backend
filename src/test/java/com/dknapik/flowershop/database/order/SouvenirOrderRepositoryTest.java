package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.database.product.SouvenirRepository;
import com.dknapik.flowershop.model.order.SouvenirOrder;
import com.dknapik.flowershop.model.product.Souvenir;
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
public class SouvenirOrderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private SouvenirRepository productRepository;
    @Autowired
    private SouvenirOrderRepository orderRepository;
    @Autowired
    private Environment env;


    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    public void saveToDatabaseTest() {
        /* Create order entity using product entity */
        Souvenir product = createSouvenirEntity();
        SouvenirOrder order = new SouvenirOrder(5, product);

        /* Save to database using repository */
        orderRepository.saveAndFlush(order);

        /* Retrieve entity and check if field match the one that was saved */
        SouvenirOrder retrievedEntity = entityManager.find(SouvenirOrder.class, order.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if entity can be retrieved from database using JPA repository.
     */
    @Test
    public void retrieveFromDatabaseTest() {
        /* Create order entity using product entity */
        Souvenir product = createSouvenirEntity();
        SouvenirOrder order = new SouvenirOrder(5, product);

        /* Save order to database */
        entityManager.persistAndFlush(order);

        /* Retrieve entity using repository and check if field match the one that was saved */
        Optional<SouvenirOrder> retrievedEntity = orderRepository.findById(order.getId());
        Assertions.assertThat(retrievedEntity.get()).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if product entities are eagerly extracted from order entity
     */
    @Test
    public void fetchProductTest() throws NoSuchElementException {
        /* Create order entity using product entity */
        Souvenir product = createSouvenirEntity();
        SouvenirOrder order = new SouvenirOrder(5, product);

        /* Save order to database */
        entityManager.persistAndFlush(order);

        /* Retrieve product entity from database and check if it matches the one that was saved in order*/
        Optional<Souvenir> searchResult = productRepository.findById(product.getId());
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(product);
    }

    /**
     * Create, save to database and retrieve from database product entity
     */
    public Souvenir createSouvenirEntity() {
        Money price = Money.of(5.55, Monetary.getCurrency(env.getProperty("app-monetary-currency")));
        Souvenir souvenir = new Souvenir("Test Souvenir Entity", price, "Exemplary description @!@#$%^'&Y*UI()+}{?>\"");
        productRepository.saveAndFlush(souvenir);
        return productRepository.findById(souvenir.getId()).get();
    }
}
