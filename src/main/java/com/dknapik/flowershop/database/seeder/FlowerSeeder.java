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
 * TODO - Figure out if there is a better way than variable dependency injection
 * Current observations:
 *  1) Annotating this class with @Component annotation will cause Spring to support this class life even thought
 *     it is used only at the start of application.
 *  2) Passing handles to objects manually through constructor would create a very large constructor
 *     in the parent class. This would decrease code readability.
 *  3) Currently used variable dependency injection forces variables without final keyword, which can cause
 *     some troubles in code readability.
 */
@Component
public class FlowerSeeder implements SeederInt {
    @Autowired
    private MoneyUtils moneyUtils;                     // Money currency retrieved from application context
    @Autowired
    private FlowerRepository flowerRepository;         // Repository for database retrieving/saving entities
    private static final boolean onlyForDebug = true;  // To check if class should be always instantiated and used
    private int numberOfEntities = 1;                  // How many entities should be inserted into database (OPTIONAL)


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

    public void setNumberOfEntities(int numberOfEntities) {
        this.numberOfEntities = numberOfEntities;
    }

    private boolean checkExistence(Flower flower) {
        return flowerRepository.findByName(flower.getName()).isPresent();
    }
}
