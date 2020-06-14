package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.model.order.DeliveryAddress;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class DeliveryAddressRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    void saveToDatabaseTest() {
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
     * Check if entity can be extracted from database using repository.
     */
    @Test
    void retrieveFromDatabase() {
        /* Create  entity */
        String name = "Test Shopping Cart";
        DeliveryAddress address =
                new DeliveryAddress("Katowice", "42-500", "ExampleStreet", "328");

        /* Save entity to database */
        entityManager.persistAndFlush(address);

        /* Retrieve entity using repository and check if it is the same as the one saved */
        Optional<DeliveryAddress> searchResult = deliveryAddressRepository.findOne(Example.of(address));
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(address);
    }
}
