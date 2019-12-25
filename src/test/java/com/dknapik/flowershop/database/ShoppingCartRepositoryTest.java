package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.ShoppingCart;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class ShoppingCartRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;


    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    public void saveToDatabaseTest() {
        /* Create entity */
        String name = "Test Shopping Cart";
        ShoppingCart shoppingCart = new ShoppingCart(name);

        /* Save entity to database using repository */
        shoppingCartRepository.saveAndFlush(shoppingCart);

        /* Retrieve entity from database and compare it to the one saved */
        ShoppingCart retrievedEntity = entityManager.find(ShoppingCart.class, shoppingCart.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(shoppingCart);
    }

    @Test
    public void retrieveFromDatabase() {
        /* Create  entity */
        String name = "Test Shopping Cart";
        ShoppingCart shoppingCart = new ShoppingCart(name);

        /* Save entity to database */
        entityManager.persistAndFlush(shoppingCart);

        /* Retrieve entity using repository and check if it is the same as the one saved */
        Optional<ShoppingCart> searchResult = shoppingCartRepository.findOne(Example.of(shoppingCart));
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(shoppingCart);
    }
}
