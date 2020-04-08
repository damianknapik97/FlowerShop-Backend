package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.order.OccasionalArticleOrderRepository;
import com.dknapik.flowershop.database.order.ShoppingCartRepository;
import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.model.AccountRole;
import com.dknapik.flowershop.model.productorder.OccasionalArticleOrder;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.ToString;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@ToString
@Transactional
class ShoppingCartSeeder implements SeederInt {
    private static final boolean ONLY_FOR_DEBUG = true;     // To check if class should be always instantiated and used
    private final MoneyUtils moneyUtils;
    private final OccasionalArticleRepository occasionalArticleRepository;
    private final OccasionalArticleOrderRepository occasionalArticleOrderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;


    @Autowired
    public ShoppingCartSeeder(MoneyUtils moneyUtils,
                              OccasionalArticleRepository occasionalArticleRepository,
                              OccasionalArticleOrderRepository occasionalArticleOrderRepository,
                              ShoppingCartRepository shoppingCartRepository,
                              AccountRepository accountRepository,
                              PasswordEncoder passwordEncoder) {
        this.moneyUtils = moneyUtils;
        this.occasionalArticleRepository = occasionalArticleRepository;
        this.occasionalArticleOrderRepository = occasionalArticleOrderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.accountRepository = accountRepository;
        this.encoder = passwordEncoder;
    }

    /* TODO: Problems with Cascades:
     *   - The first one : CascadeType.Persit does not persist Occasional Article Order inside shopping cart
     *   - The second one: CascadeType.Merge  persist Ocassional Article Order inside shopping cart (why ?)
     *   - The third one: Removing Account together with shopping cart, for some reason creates new Occasional Article Order entity */
    @Override
    public void seed() {
//        String userName = "user";
//        Account userAccount = retrieveAccount(userName);
//        boolean shoppingCartEmpty = isShoppingCartEmpty(userAccount);
//
//        /* Try to retrieve shopping cart and create new one if doesn't exist */
//        if (shoppingCartEmpty) {
//            /* Retrieve/Create Occasional Article from/in database */
//            Money price = Money.of(2, moneyUtils.getApplicationCurrencyUnit());
//            OccasionalArticle occasionalArticle =
//                    retrieveOccasionalArticle("ShoppingCartArticle",
//                            price,
//                            "This item is great for specific occasion.",
//                            "New Year");
//
//            /* Create new Occasional Article Order and add it to the list */
//            OccasionalArticleOrder newOrder = new OccasionalArticleOrder(2, occasionalArticle);
//            List<OccasionalArticleOrder> occasionalArticleOrderList = new LinkedList<>();
//            occasionalArticleOrderList.add(newOrder);
//
//            /* Create new shopping cart object, populated by id and occasional article orders */
//            userAccount.getShoppingCart().setName("TEST");
//            userAccount.getShoppingCart().setOccasionalArticleOrderList(occasionalArticleOrderList);
//            accountRepository.saveAndFlush(userAccount);
//        }
    }

    /**
     * Check if provided shopping cart contains any products inside it.
     *
     * @param userAccount - Account containing shopping cart
     * @return true if there is at least one product inside shopping cart
     */
    private boolean isShoppingCartEmpty(Account userAccount) {
        boolean souvenirsEmpty = true;
        if (userAccount.getShoppingCart().getSouvenirOrderList() != null) {
            souvenirsEmpty = userAccount.getShoppingCart().getSouvenirOrderList().isEmpty();
        }

        boolean occasionalArticleEmpty = true;
        if (userAccount.getShoppingCart().getOccasionalArticleOrderList() != null) {
            occasionalArticleEmpty = userAccount.getShoppingCart().getOccasionalArticleOrderList().isEmpty();
        }

        boolean flowersEmpty = true;
        if (userAccount.getShoppingCart().getFlowerOrderList() != null) {
            flowersEmpty = userAccount.getShoppingCart().getFlowerOrderList().isEmpty();
        }

        boolean bouquetsEmpty = true;
        if (userAccount.getShoppingCart().getBouquetList() != null) {
            bouquetsEmpty = userAccount.getShoppingCart().getBouquetList().isEmpty();
        }

        return souvenirsEmpty && occasionalArticleEmpty && flowersEmpty && bouquetsEmpty;
    }

    /**
     * Search for provided account name. If provided account name doesn't match any entity, create new one
     *
     * @param accountName
     * @return Account entity;
     */
    private Account retrieveAccount(String accountName) {
        Account toReturn;
        if (!this.accountRepository.existsByName(accountName)) {
            toReturn = new Account(accountName,
                    encoder.encode(accountName),
                    "user@test.pl",
                    AccountRole.ROLE_USER);
            accountRepository.saveAndFlush(toReturn);
        } else {
            toReturn = this.accountRepository.findByName(accountName).get();
        }
        return toReturn;
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
        OccasionalArticle occasionalArticle;
        Optional<OccasionalArticle> retrievedOccasionalArticle =
                occasionalArticleRepository.findByNameAndDescriptionAndTheme(name, description, theme);

        if (!retrievedOccasionalArticle.isPresent()) {
            occasionalArticle = new OccasionalArticle(name, price, description, theme);
            occasionalArticleRepository.saveAndFlush(occasionalArticle);
        } else {
            occasionalArticle = retrievedOccasionalArticle.get();
        }
        return occasionalArticle;
    }

    @Override
    public boolean isOnlyForDebug() {
        return ONLY_FOR_DEBUG;
    }
}
