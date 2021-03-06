package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.product.AddonRepository;
import com.dknapik.flowershop.model.product.Addon;
import com.dknapik.flowershop.model.product.AddonColour;
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
@ToString
@Component
class AddonSeeder implements SeederInt {
    private final MoneyUtils moneyUtils;               // Money currency retrieved from application context
    private final AddonRepository addonRepository;           // Repository for database retrieving/saving entities
    private static final boolean ONLY_FOR_DEBUG = true;  // To check if class should be always instantiated and used

    @Autowired
    public AddonSeeder(MoneyUtils moneyUtils, AddonRepository addonRepository) {
        this.moneyUtils = moneyUtils;
        this.addonRepository = addonRepository;
    }

    @Override
    public void seed() {
        /* Create array with entities to save */
        Money price = Money.of(2, moneyUtils.getApplicationCurrencyUnit());
        String description = "The ribbon is a symbol of high quality.";

        Addon[] addonArray = {
                new Addon("Ribbon", AddonColour.BLUE, price, description),
                new Addon("Ribbon", AddonColour.RED, price, description),
                new Addon("Bow", AddonColour.BLUE, price, description),
        };

        /* Check if entity already exists and save it if not */
        for (Addon addon : addonArray) {
            if (!checkIfExists(addon)) {
                addonRepository.save(addon);
            }
        }

        /* Flush all changes */
        addonRepository.flush();
    }

    @Override
    public boolean isOnlyForDebug() {
        return ONLY_FOR_DEBUG;
    }

    /* Check if value already exists in database */
    private boolean checkIfExists(Addon addon) {
        return addonRepository.findByNameAndColour(addon.getName(), addon.getColour()) != null;
    }
}
