package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.product.OccasionalArticleRepository;
import com.dknapik.flowershop.model.product.OccasionalArticle;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.ToString;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ToString
class OccasionalArticleSeeder implements SeederInt {
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
        OccasionalArticle[] occasionalArticlesArray = createOccasionalArticles();

        /* Check if entity already exists and save it if not */
        for (OccasionalArticle occasionalArticle : occasionalArticlesArray) {
            if (!checkIfExists(occasionalArticle)) {
                occasionalArticleRepository.save(occasionalArticle);
            }
        }

        /* Flush all changes */
        occasionalArticleRepository.flush();
    }

    private OccasionalArticle[] createOccasionalArticles() {
        return new OccasionalArticle[] {
                new OccasionalArticle("Card", moneyUtils.amountWithAppCurrency(4.99),
                        "This product contains big \"Thank You\" inscription up front. " +
                                "We will add your message on the back of this card", "Any",
                        "https://drive.google.com/uc?id=1tZy6GiJ1FDETKiKRxS_rdjV1-EVQGQMk",
                        "https://drive.google.com/uc?id=1_mtJlBeka2YuYrwg-JuJZN_0Z9mgRAn-",
                        "https://drive.google.com/uc?id=1jxKIMOd3bAJRFxAJZxpTBkHQ6H2j-Rnj"),
                new OccasionalArticle("Card", moneyUtils.amountWithAppCurrency(4.99),
                        "This product contains big \"Happy Easter\" inscription, with hearths around it. " +
                                "We will add your message on the back of this card", "Easter",
                        "https://drive.google.com/uc?id=1ZAmqe0WxXsTLVkNzsoQBTVm4a0cnH9If",
                        "https://drive.google.com/uc?id=1mpimGYNZQhIB1VbV0oMhYkx-JanjZEBV",
                        "https://drive.google.com/uc?id=1v1RycRiCx8JsWix18AXL6axB14l9PW85"),
                new OccasionalArticle("Card", moneyUtils.amountWithAppCurrency(4.99),
                        "This product contains big \"Happy Birthday\" inscriptions up front. " +
                                "We will add your message on the back of this card", "Birthday",
                        "https://drive.google.com/uc?id=1uDCprDMblmGUquyk4plGnCumvuwvQ5mC",
                        "https://drive.google.com/uc?id=1zVILb7eDo49Xs1w-g2WwXMeb8n-av5gt",
                        "https://drive.google.com/uc?id=1EttzbOvuxqDfFSnEZ9sVatbJC9n4xLDN"),
                new OccasionalArticle("Emoji Balloons", moneyUtils.amountWithAppCurrency(1.99),
                        "Two emoji shaped balloons representing \"Kiss Emoji\" and \"Hearth Eyes Emoji\" " +
                                "filled with helium", "Any",
                        "https://drive.google.com/uc?id=1CaKK0bLpF9geDwuGbWuT84L1ZfPTwbga",
                        "https://drive.google.com/uc?id=1BlFd5YfX4sKjD77E7E33FJlO_h67JlIR",
                        "https://drive.google.com/uc?id=1i9XSEvVgoe85SzsYbvFstRFt1xPZoaCi"),
                new OccasionalArticle("Colorful Balloons", moneyUtils.amountWithAppCurrency(4.99),
                        "Pack of ten helium filled balloons. Colours are chosen at random.", "Any",
                        "https://drive.google.com/uc?id=1denXo5IArSy8aQmeO2zc4WvDfm-4e-33",
                        "https://drive.google.com/uc?id=1QR-0caRcjIGlB1Qp75f4gHTowx4Yflef",
                        "https://drive.google.com/uc?id=11S8BtkRn8XyZIIzByVcj7x5g4OLRgpbu")
        };
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
