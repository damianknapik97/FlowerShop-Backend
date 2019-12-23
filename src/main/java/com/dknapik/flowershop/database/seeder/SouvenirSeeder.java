package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.product.SouvenirRepository;
import com.dknapik.flowershop.model.product.Souvenir;
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
public class SouvenirSeeder implements SeederInt {
    private final MoneyUtils moneyUtils;                  // Money currency retrieved from application context
    private final SouvenirRepository souvenirRepository;  // Repository for database retrieving/saving entities
    private static final boolean onlyForDebug = true;     // To check if class should be always instantiated and used


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
                new Souvenir("Pillow", price, description)
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
        return onlyForDebug;
    }

    /* Check if value already exists in database */
    private boolean checkIfExists(Souvenir souvenir) {
        return souvenirRepository.findByName(souvenir.getName()).isPresent();
    }

}