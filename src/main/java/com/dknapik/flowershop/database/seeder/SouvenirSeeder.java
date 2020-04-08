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
class SouvenirSeeder implements SeederInt {
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
        Souvenir[] souvenirArray = createSouvnirs();

        /* Check if entity already exists and save it if not */
        for (Souvenir souvenir : souvenirArray) {
            if (!checkIfExists(souvenir)) {
                souvenirRepository.save(souvenir);
            }
        }

        /* Flush all changes */
        souvenirRepository.flush();
    }

    private Souvenir[] createSouvnirs() {
        return new Souvenir[] {
                new Souvenir("Standing man figurine", moneyUtils.amountWithAppCurrency(19.99),
                        "Silver figurine representing muscular man. Perfect gift for honoring an archivement",
                        "https://drive.google.com/uc?id=19hB2Xi6O1jwQT8ZC9OyTuoTicXZ_SyT3",
                        "https://drive.google.com/uc?id=1zuDRjkIUK7XZJ6etBhjsVJX5mpvvq86g",
                        "https://drive.google.com/uc?id=1d-4SJrtmdEmg9r6dWbpjxVOaKQ2drAGQ"),
                new Souvenir("Beige Teddy Bear", moneyUtils.amountWithAppCurrency(16.00),
                        "Fluffy, medium sized decorative item. Looks great on any shelf.",
                        "https://drive.google.com/uc?id=1Ca4DwNPV1RLJgZmYCM8aBEKNRD45dBE6",
                        "https://drive.google.com/uc?id=1zLl4KItIv5_DnsxTXv60tg4dqU8eJU-I",
                        "https://drive.google.com/uc?id=1Ni13SKH5jklfceXJQNNj1FISnK9gHiMx"),
                new Souvenir("Brown Teddy Bear", moneyUtils.amountWithAppCurrency(9.99),
                        "Fluffy, small decorative item. Looks great on any shelf.",
                        "https://drive.google.com/uc?id=1G9VH9g-cfvV-lHdX67OkP6oxCfFjMFMs",
                        "https://drive.google.com/uc?id=1dypiV-FMS4hKPr3tPPqcqEvhUvNQ05dN",
                        "https://drive.google.com/uc?id=1v20jlxlVgoxra8FtAj2TimtCQwn4H4tA"),
                new Souvenir("Mermaid Cat Pillow", moneyUtils.amountWithAppCurrency(15.00),
                        "Funny, medium sized pillow.",
                        "https://drive.google.com/uc?id=1AUw4JZ2ts9sHJXX0hYpbVk81u0ttEaOn",
                        "https://drive.google.com/uc?id=1Lu5t2Y8OULvoO7UQwmsH4YfZsthaTHvA",
                        "https://drive.google.com/uc?id=1AUw4JZ2ts9sHJXX0hYpbVk81u0ttEaOn"),
                new Souvenir("Scooter Dog Pillow", moneyUtils.amountWithAppCurrency(12.00),
                        "Funny, medium sized pillow.",
                        "https://drive.google.com/uc?id=13sNpdgpvwXvTYNdcr3fhGIxt6fKkfeRB",
                        "https://drive.google.com/uc?id=1Mqz8NclVHwnx7HRoqO80jIW5A0v-ErwC",
                        "https://drive.google.com/uc?id=1T0W3of4kVGbLPb9ERsqbaXMsWXhovb_N")
        };
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
