package com.dknapik.flowershop.database.order;

import com.dknapik.flowershop.model.order.Payment;
import com.dknapik.flowershop.model.order.PaymentType;
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
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {"app-monetary-currency=PLN"})
class PaymentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private Environment env;

    /**
     * Check if entity can be saved to database without any undesired changes or errors
     */
    @Test
    void saveToDatabaseTest() {
        Money money = Money.of(32.32, Monetary.getCurrency(env.getProperty("app-monetary-currency")));

        /* Create entity */
        Payment payment = new Payment(money, PaymentType.BANK_TRANSFER);

        /* Save entity to database using repository */
        paymentRepository.saveAndFlush(payment);

        /* Retrieve entity from database and compare it to the one saved */
        Payment retrievedEntity = entityManager.find(Payment.class, payment.getId());
        Assertions.assertThat(retrievedEntity).isEqualToComparingFieldByField(payment);
    }

    /**
     * Check if entity can be extracted from database using repository.
     */
    @Test
    void retrieveFromDatabase() {
        Money money = Money.of(32.32, Monetary.getCurrency(env.getProperty("app-monetary-currency")));

        /* Create  entity */
        Payment payment = new Payment(money, PaymentType.BANK_TRANSFER);

        /* Save entity to database */
        entityManager.persistAndFlush(payment);

        /* Retrieve entity using repository and check if it is the same as the one saved */
        Optional<Payment> searchResult = paymentRepository.findById(payment.getId());
        Assertions.assertThat(searchResult.get()).isEqualToComparingFieldByField(payment);
    }
}
