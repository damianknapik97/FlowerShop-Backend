package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.utility.MoneyUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for seeding database with some debug/test values
 *
 * @author Damian
 */
@Component
public class FlowerSeeder implements SeederInt {
    private MoneyUtils moneyUtils;                     // Money currency retrieved from application context
    private FlowerRepository flowerRepository;         // Repository for database retrieving/saving entities
    private static final boolean onlyForDebug = true;  // To check if class should be always instantiated and used
    private int numberOfEntities = 1;                  // How many entities should be inserted into database (OPTIONAL)


    @Autowired
    public FlowerSeeder(MoneyUtils moneyUtils, FlowerRepository flowerRepository) {
        this.moneyUtils = moneyUtils;
        this.flowerRepository = flowerRepository;
    }

    @Override
    public void seed() {
        /* Create overall entity configuration  */
        String flowerName = "Rose" ;
        Money price = Money.of(5, moneyUtils.getApplicationCurrencyUnit());
        String description = "Long a symbol of love and passion, the ancient Greeks and Romans associated roses with" +
                " Aphrodite and Venus, goddess of love. Used for hundreds of years to convey messages without words," +
                " they also represent confidentiality. In fact, the Latin expression \"sub rosa\"(literally," +
                " \"under the rose\") means something told in secret, and in ancient Rome, a wild rose was placed" +
                " on the door to a room where confidential matters were being discussed.";
        int height = 5;
        Flower newFlower;

        /* Create and save to database defined number of entities only with changed name */
        for (int i = 0; i < numberOfEntities; i++) {
            newFlower = new Flower(flowerName + i, price, description, height);
            /* Check if already exists, and save if not */
            if (!checkExistence(newFlower)) {
                flowerRepository.save(newFlower);
            }
        }
        flowerRepository.flush();
    }

    @Override
    public boolean isOnlyForDebug() {
        return onlyForDebug;
    }

    public int getNumberOfEntities() {
        return numberOfEntities;
    }

    public FlowerSeeder setNumberOfEntities(int numberOfEntities) {
        this.numberOfEntities = numberOfEntities;
        return this;
    }

    /* Check if value already exists in database */
    private boolean checkExistence(Flower flower) {
        return flowerRepository.findByName(flower.getName()).isPresent();
    }
}
