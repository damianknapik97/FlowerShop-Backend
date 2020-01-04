package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.order.OccasionalArticleOrderRepository;
import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.model.order.OccasionalArticleOrder;
import com.dknapik.flowershop.model.order.ShoppingCart;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.utility.MoneyUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class ShoppingCartSeeder implements SeederInt {
    private static final boolean ONLY_FOR_DEBUG = true;     // To check if class should be always instantiated and used
    private final MoneyUtils moneyUtils;
    private final OccasionalArticleRepository occasionalArticleRepository;
    private final OccasionalArticleOrderRepository occasionalArticleOrderRepository;
    private final ShoppingCartRepository shoppingCartRepository;


    @Autowired
    public ShoppingCartSeeder(MoneyUtils moneyUtils,
                              OccasionalArticleRepository occasionalArticleRepository,
                              OccasionalArticleOrderRepository occasionalArticleOrderRepository,
                              ShoppingCartRepository shoppingCartRepository) {
        this.moneyUtils = moneyUtils;
        this.occasionalArticleRepository = occasionalArticleRepository;
        this.occasionalArticleOrderRepository = occasionalArticleOrderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public void seed() {
        /* Retrieve Occasional Article from database */
        String shoppingCartName = "Test Shopping Cart";
        Money price = Money.of(2, moneyUtils.getApplicationCurrencyUnit());
        OccasionalArticle occasionalArticle =
                retrieveOccasionalArticle("Card",
                        price,
                        "This item is great for specific occasion.",
                        "New Year");


        /* Create new Occasional Article Order and add it to the list */
        OccasionalArticleOrder newOrder = new OccasionalArticleOrder(2, occasionalArticle);
        List<OccasionalArticleOrder> occasionalArticleOrderList = new LinkedList<>();
        occasionalArticleOrderList.add(newOrder);

        /* Search if Testing Shopping Cart already exists and save it if not */
        ShoppingCart testingShoppingCart = new ShoppingCart(shoppingCartName, occasionalArticleOrderList);
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findOne(Example.of(testingShoppingCart));
        if (!shoppingCart.isPresent()) {
            shoppingCartRepository.saveAndFlush(testingShoppingCart);
        }
    }

    /**
     * Search for OccasionalArticle with provided values in database, and create and save new if search failed.
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
        return ONLY_FOR_DEBUG;
    }
}
