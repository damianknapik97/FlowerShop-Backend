package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.OccasionalArticleOrderRepository;
import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.model.OccasionalArticleOrder;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.utility.MoneyUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *  TODO: This seeder is probably useless as it doesn't interact with user in any way and is only part of other entities
 *
 */
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
        /* Retrieve Occasional Article from database */
        Money price = Money.of(2, moneyUtils.getApplicationCurrencyUnit());
        OccasionalArticle occasionalArticle =
                retrieveOccasionalArticle("Card",
                                           price,
                                          "This item is great for specific occasion.",
                                          "New Year");


        /* Create new order entity, check if it already exists and save it to database if not */
        OccasionalArticleOrder occasionalArticleOrder = new OccasionalArticleOrder(1, occasionalArticle);
        Optional<OccasionalArticleOrder> searchResult = orderRepository.findOne(Example.of(occasionalArticleOrder));
        if (!searchResult.isPresent()) {
            orderRepository.saveAndFlush(occasionalArticleOrder);
        }
    }

    /**
     *
     *  Search for OccasionalArticle with provided values in database, and create and save new if search failed.
     *
     * @param name
     * @param price
     * @param description
     * @param theme
     * @return Occasional Article entity saved in database.
     */
    private OccasionalArticle retrieveOccasionalArticle(String name, Money price, String description, String theme) {
        Optional<OccasionalArticle> occasionalArticle =
                occasionalArticleRepository.findByNameAndDescriptionAndTheme(name, description, theme);

        if (!occasionalArticle.isPresent()) {
            occasionalArticle = Optional.of(new OccasionalArticle(name, price, description, theme));
            occasionalArticleRepository.saveAndFlush(occasionalArticle.get());
        }

        return occasionalArticle.get();
    }

    @Override
    public boolean isOnlyForDebug() {
        return onlyForDebug;
    }
}
