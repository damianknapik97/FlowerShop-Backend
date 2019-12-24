package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.OccasionalArticleOrderRepository;
import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.model.OccasionalArticleOrder;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.utility.MoneyUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OccasionalArticleOrderSeeder implements SeederInt {
    private final MoneyUtils moneyUtils;                  // Money currency retrieved from application context
    private final OccasionalArticleOrderRepository orderRepository;        // Repository for retrieving/saving entities
    private final OccasionalArticleRepository occasionalArticleRepository; // Repository for retrieving/saving entities
    private static final boolean onlyForDebug = true;     // To check if class should be always instantiated and used


    @Autowired
    public OccasionalArticleOrderSeeder(MoneyUtils moneyUtils,
                                        OccasionalArticleOrderRepository occasionalArticleRepository,
                                        OccasionalArticleRepository occasionalArticleRepository1) {
        this.moneyUtils = moneyUtils;
        this.orderRepository = occasionalArticleRepository;
        this.occasionalArticleRepository = occasionalArticleRepository1;
    }

    @Override
    public void seed() {
        /* Create array with entities to save */
        String name = "Card";
        Money price = Money.of(2, moneyUtils.getApplicationCurrencyUnit());
        String description = "This item is great for specific occasion.";
        String theme = "New Year";
        Optional<OccasionalArticle> occasionalArticle =
                occasionalArticleRepository.findByNameAndDescriptionAndTheme(name, description, theme);


        if (!occasionalArticle.isPresent()) {
            occasionalArticle = Optional.of(new OccasionalArticle(name, price, description, theme));
            occasionalArticleRepository.saveAndFlush(occasionalArticle.get());
        }

        OccasionalArticleOrder occasionalArticleOrder =
                new OccasionalArticleOrder(1, occasionalArticle.get());

        orderRepository.saveAndFlush(occasionalArticleOrder);
    }

    @Override
    public boolean isOnlyForDebug() {
        return onlyForDebug;
    }

}
