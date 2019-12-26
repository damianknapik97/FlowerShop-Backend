package com.dknapik.flowershop.database;

import com.dknapik.flowershop.model.DeliveryAddress;
import com.dknapik.flowershop.model.ShoppingCart;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DeliveryAddressRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    public void saveToDatabaseTest() {
        /* Create entity */
        DeliveryAddress address =
                new DeliveryAddress("Katowice", "42-500", "ExampleStreet", "328");

        /* Save entity to database using repository */
        deliveryAddressRepository.saveAndFlush(address);

        /* Retrieve entity from database and compare it to the one saved */
        DeliveryAddress retrievedEntity = entityManager.find(DeliveryAddress.class, address.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(address);
    }

    /**
     *  Check if entity can be extracted from database using repository.
     */
    @Test
    public void retrieveFromDatabase() {
        /* Create  entity */
        String name = "Test Shopping Cart";
        DeliveryAddress address =
                new DeliveryAddress("Katowice", "42-500", "ExampleStreet", "328");

        /* Save entity to database */
        entityManager.persistAndFlush(address);

        /* Retrieve entity using repository and check if it is the same as the one saved */
        Optional<DeliveryAddress> searchResult =deliveryAddressRepository.findOne(Example.of(address));
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(address);
    }


}
