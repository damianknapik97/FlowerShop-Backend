package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.product.SouvenirRepository;
import com.dknapik.flowershop.model.product.Souvenir;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.ToString;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for seeding database with some debug/test values
 *
 * @author Damian
 */

@Component
@ToString
public class SouvenirSeeder implements SeederInt {
    private final MoneyUtils moneyUtils;                  // Money currency retrieved from application context
    private final SouvenirRepository souvenirRepository;  // Repository for database retrieving/saving entities
    private static final boolean ONLY_FOR_DEBUG = true;     // To check if class should be always instantiated and used


    @Autowired
    public SouvenirSeeder(MoneyUtils moneyUtils, SouvenirRepository souvenirRepository) {
        this.moneyUtils = moneyUtils;
        this.souvenirRepository = souvenirRepository;
    }

    @Override
    public void seed() {
        /* Create array with entities to save */
        Money price = Money.of(2, moneyUtils.getApplicationCurrencyUnit());
        String description = "This item is great for any occasion.";
        Souvenir[] souvenirArray = {
                new Souvenir("Cup", price, description),
                new Souvenir("Perfume", price, description),
                new Souvenir("Pillow", price, description),
                new Souvenir("Hearth shaped pillow", price, description),
                new Souvenir("Round pillow", price, description)
        };

        /* Check if entity already exists and save it if not */
        for (Souvenir souvenir : souvenirArray) {
            if (!checkIfExists(souvenir)) {
                souvenirRepository.save(souvenir);
            }
        }

        /* Flush all changes */
        souvenirRepository.flush();
    }

    @Override
    public boolean isOnlyForDebug() {
        return ONLY_FOR_DEBUG;
    }

    /* Check if value already exists in database */
    private boolean checkIfExists(Souvenir souvenir) {
        return souvenirRepository.findByName(souvenir.getName()).isPresent();
    }

}
