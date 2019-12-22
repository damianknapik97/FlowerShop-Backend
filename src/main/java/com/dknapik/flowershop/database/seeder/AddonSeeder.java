package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.product.AddonRepository;
import com.dknapik.flowershop.model.product.Addon;
import com.dknapik.flowershop.utility.MoneyUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
public class AddonSeeder implements SeederInt  {
    @Autowired
    private MoneyUtils moneyUtils;                     // Money currency retrieved from application context
    @Autowired
    private AddonRepository addonRepository;           // Repository for database retrieving/saving entities
    private static final boolean onlyForDebug = true;  // To check if class should be always instantiated and used


    @Override
    public void seed() {
        Money price = Money.of(2, moneyUtils.getApplicationCurrencyUnit());
        String description = "The ribbon is a symbol of high quality.";

        Addon[] addonArray = {
                new Addon("Ribbon", "Blue", price, description),
                new Addon("Ribbon", "Red", price, description),
                new Addon("Bow", "Blue", price, description),
        };

        for (Addon addon : addonArray) {
            if (!checkIfExists(addon)) {
                addonRepository.save(addon);
            }
        }

        addonRepository.flush();
    }

    @Override
    public boolean isOnlyForDebug() {
        return onlyForDebug;
    }

    private boolean checkIfExists(Addon addon) {
        return addonRepository.findByNameAndColour(addon.getName(), addon.getColour()) != null;
    }
}
