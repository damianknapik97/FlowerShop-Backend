package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.money.Monetary;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
public class ShoppingCartRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private OccasionalArticleRepository productRepository;
    @Autowired
    private OccasionalArticleOrderRepository orderRepository;
    @Autowired
    private Environment env;

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

    /**
     * Check if entity can be extracted from database using repository.
     */
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

    /**
     * Check if order entities that are part of shopping cart entity, will be saved into database with shopping cart,
     * and can be retrieved later separately.
     */
    @Test
    public void occasionalArticleOrderCascadePersistTest() {
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));

        /* Get product entity */
        OccasionalArticle product = retrieveOccasionalArticleEntity("Occasional Article",
                money,
                "Test Description",
                "Test Theme");

        /* Create order entity by using product entity */
        OccasionalArticleOrder order = new OccasionalArticleOrder(2, product);
        List<OccasionalArticleOrder> orderList = new LinkedList<>();
        orderList.add(order);

        /* Create shopping cart entity */
        ShoppingCart shoppingCart = new ShoppingCart("Test Shopping Cart", orderList);

        /* Save shopping cart entity and check if order entity was also saved and is retrievable from database */
        shoppingCartRepository.saveAndFlush(shoppingCart);
        Optional<OccasionalArticleOrder> searchResults = orderRepository.findById(order.getId());
        Assertions.assertThat(searchResults.get()).isEqualToComparingFieldByField(order);
    }

    /**
     * Check if associated order entities will also be deleted when shopping cart entity is deleted.
     */
    @Test
    public void occasionalArticleOrderCascadeRemoveTest() {
        Money money = Money.of(10, Monetary.getCurrency(env.getProperty("app-monetary-currency")));

        /* Get product entity */
        OccasionalArticle product = retrieveOccasionalArticleEntity("Occasional Article",
                money,
                "Test Description",
                "Test Theme");

        /* Create order entity by using product entity */
        OccasionalArticleOrder order = new OccasionalArticleOrder(2, product);
        List<OccasionalArticleOrder> orderList = new LinkedList<>();
        orderList.add(order);

        /* Create and save shopping cart entity */
        ShoppingCart shoppingCart = new ShoppingCart("Test Shopping Cart", orderList);
        shoppingCartRepository.saveAndFlush(shoppingCart);

        /* Remove shopping cart entity and search database for order entity which should be also removed */
        shoppingCartRepository.delete(shoppingCart);
        Optional<OccasionalArticleOrder> searchResult = orderRepository.findById(order.getId());

        Assertions.assertThat(searchResult.isPresent()).isFalse();

    }

    private OccasionalArticle retrieveOccasionalArticleEntity(String name,
                                                              Money price,
                                                              String description,
                                                              String theme) {
        /* Create and save/Retrieve product entity */
        Optional<OccasionalArticle> product =
                productRepository.findByNameAndDescriptionAndTheme(name, description, theme);
        if (!product.isPresent()) {
            product = Optional.of(new OccasionalArticle(name, price, description, theme));
            productRepository.saveAndFlush(product.get());
        }

        return product.get();
    }
}
