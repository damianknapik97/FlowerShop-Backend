package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.utility.MoneyUtils;
import lombok.ToString;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ToString
public class OccasionalArticleSeeder implements SeederInt {
    private final MoneyUtils moneyUtils;                  // Money currency retrieved from application context
    private final OccasionalArticleRepository occasionalArticleRepository;  // Repository for retrieving/saving entities
    private static final boolean ONLY_FOR_DEBUG = true;     // To check if class should be always instantiated and used


    @Autowired
    public OccasionalArticleSeeder(MoneyUtils moneyUtils, OccasionalArticleRepository occasionalArticleRepository) {
        this.moneyUtils = moneyUtils;
        this.occasionalArticleRepository = occasionalArticleRepository;
    }

    @Override
    public void seed() {
        /* Create array with entities to save */
        Money price = Money.of(2, moneyUtils.getApplicationCurrencyUnit());
        String description = "This item is great for specific occasion.";
        OccasionalArticle[] occasionalArticlesArray = {
                new OccasionalArticle("Card", price, description, "Birthday"),
                new OccasionalArticle("Card", price, description, "Christmas"),
                new OccasionalArticle("Confetti", price, description, "Birthday")
        };

        /* Check if entity already exists and save it if not */
        for (OccasionalArticle occasionalArticle : occasionalArticlesArray) {
            if (!checkIfExists(occasionalArticle)) {
                occasionalArticleRepository.save(occasionalArticle);
            }
        }

        /* Flush all changes */
        occasionalArticleRepository.flush();
    }

    @Override
    public boolean isOnlyForDebug() {
        return ONLY_FOR_DEBUG;
    }

    /* Check if value already exists in database */
    private boolean checkIfExists(OccasionalArticle occasionalArticle) {
        return occasionalArticleRepository.findByNameAndDescriptionAndTheme(occasionalArticle.getName(),
                occasionalArticle.getDescription(),
                occasionalArticle.getTheme())
                .isPresent();
    }
}
