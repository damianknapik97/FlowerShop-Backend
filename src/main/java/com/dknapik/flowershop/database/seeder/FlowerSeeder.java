package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.ToString;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for seeding database with some debug/test values
 *
 * @author Damian
 */
@ToString
@Component
class FlowerSeeder implements SeederInt {
    private MoneyUtils moneyUtils;                     // Money currency retrieved from application context
    private FlowerRepository flowerRepository;         // Repository for database retrieving/saving entities
    private static final boolean ONLY_FOR_DEBUG = true;  // To check if class should be always instantiated and used
    private int numberOfEntities = 1;           // How many entities should be inserted into database (OPTIONAL)


    @Autowired
    public FlowerSeeder(MoneyUtils moneyUtils, FlowerRepository flowerRepository) {
        this.moneyUtils = moneyUtils;
        this.flowerRepository = flowerRepository;
    }

    @Override
    public void seed() {
        /* Initialize various flower examples */
        Flower[] flowerArray = createFlowers();
        for (Flower flower: flowerArray) {
            /* Check if already exists, and save if not */
            if (!checkExistence(flower)) {
                flowerRepository.save(flower);
            }
        }

        /* Initialize the same flower with different number in order to test pagination */
        List<Flower> flowerList = createExemplaryFlowers(numberOfEntities);
        for (Flower flower: flowerList) {
            /* Check if already exists, and save if not */
            if (!checkExistence(flower)) {
                flowerRepository.save(flower);
            }
        }
        flowerRepository.flush();
    }

    private Flower[] createFlowers() {
        return new Flower[] {
                new Flower("Yellow Chrysanthemum", moneyUtils.amountWithAppCurrency(4.99),
                        "Native to Asia and northeastern Europe. In Chinese culture yellow chrysanthemum symbolized " +
                                "slighted love.", 2,
                        "https://drive.google.com/uc?id=1iM3wVzEvqwCbtgHlWqwAYp8zUezib0d9",
                        "https://drive.google.com/uc?id=1eVlbL-Gtx9J3WcbGiHmPViYr9sdfRHy9",
                        "https://drive.google.com/uc?id=1BB9AQItUo2bD7K_tf1KNQaZ-nrlBsCkB"),
                new Flower("Yellow Rose", moneyUtils.amountWithAppCurrency(5.99),
                        "Symbol of a yellow rose represents friendship, joy and caring. " +
                                "These beautiful sun-colored roses can also convey warmth, delight, gladness " +
                                "and affection, as well as say good luck, welcome back, and remember me.", 2,
                        "https://drive.google.com/uc?id=1EugrRPxPYoOsJcGKswwuI1KQ2e53Vgoq",
                        "https://drive.google.com/uc?id=1i3ZSuYPO6bURthc0xX_RV2LF8zw1xrT1",
                        "https://drive.google.com/uc?id=1bCnY45c_E1BUhJcMcYQz21CXQJHgBRW2"),
                new Flower("White Rose", moneyUtils.amountWithAppCurrency(5.99),
                        "White roses often represent purity, innocence and youthfulness. " +
                                "White roses are sometimes referred to as bridal roses because of their association " +
                                "with young love and eternal loyalty.", 2,
                        "https://drive.google.com/uc?id=108a-t938yV1ZoUQhAT3ug-mFdMM3LnB0",
                        "https://drive.google.com/uc?id=1rmYds2s0ZfpIMXwon1Fu19B81RV_GYvF",
                        "https://drive.google.com/uc?id=1YRcBfuwxLhA4R8gnvAC0XAnSrOxPYYAw"),
                new Flower("White Daffodil", moneyUtils.amountWithAppCurrency(5.50),
                        "The daffodil symbolizes rebirth and new beginnings. The Latin name " +
                                "for daffodil is Narcissus. " + "It is believed to be named after the son of the " +
                                "river god from Greek mythology.", 2,
                        "https://drive.google.com/uc?id=1ftQGAzQaHn4wKAQX3rHtidNZ7vKXX5ox",
                        "https://drive.google.com/uc?id=1MtGhNSdssxQPSopQgl58csK2xgMe2yIk",
                        "https://drive.google.com/uc?id=1JqJpvQZRm1mg2XwUE6TIYGSPIqNFTOM0"),
                new Flower("Sunflower", moneyUtils.amountWithAppCurrency(7.00),
                        "Sunflowers symbolize adoration, loyalty and longevity. " +
                                "Much of the meaning of sunflowers stems from its namesake, the sun itself.", 2,
                        "https://drive.google.com/uc?id=1cNttpdfQoy9348R148Izkkp-S_QI-18i",
                        "https://drive.google.com/uc?id=1h3RAHZLTgHu8tl2l0jK_h7E45LS_TJP3",
                        "https://drive.google.com/uc?id=1VOtORAj2Gz6CaHK4MnB_oJJFFOidY-Wo"),
                new Flower("Red Dahlia", moneyUtils.amountWithAppCurrency(6.00),
                        "Red Dahlia flower symbolizes strength and power that we want to give to the person " +
                                "who is receiving this flower. Color red in general symbolizes love and passion, " +
                                "but it can also be a symbol of power, strength and importance.", 2,
                        "https://drive.google.com/uc?id=19mGdu5v0Chr1V80QzF9lc6NGRca4w2eT",
                        "https://drive.google.com/uc?id=1XwUxTEsn7a5VSwWjcJ5QvV-7HMb3iuIm",
                        "https://drive.google.com/uc?id=1T4GWdew-8I1lN1OL0Q9Lv4nuOd1mE2Jk"),
                new Flower("Red Tulip", moneyUtils.amountWithAppCurrency(4.00),
                        "The meaning of tulips is generally perfect love. " +
                                "Red tulips are most strongly associated with true love.", 2,
                        "https://drive.google.com/uc?id=1CeLRvOLPK12CmEYseTx0JwCPLZ22i6gN",
                        "https://drive.google.com/uc?id=1TeN6Pxq2ClknENp0qQ9F20Bix5natk3z",
                        "https://drive.google.com/uc?id=1xKQP8UVAQvY6ur8bbevZ1sAKHbNxprIF"),
                new Flower("Red Rose", moneyUtils.amountWithAppCurrency(5.99),
                        "Long a symbol of love and passion, the ancient Greeks and Romans " +
                                "associated roses with Aphrodite and Venus, goddess of love. Used for " +
                                "hundreds of years to convey messages without words).", 2,
                        "https://drive.google.com/uc?id=1Ln_5TTKfqJOyYCbMpMbMPXfnPB3QQV6v",
                        "https://drive.google.com/uc?id=1wrLYEywIo01k3rYdcgPJqPYwuBztploF",
                        "https://drive.google.com/uc?id=10gbAq-CTFRSGMl8XJFQZItjxJP8nYqmX"),
                new Flower("Red Carnation", moneyUtils.amountWithAppCurrency(5.99),
                        "For the most part, carnations express love, fascination, and distinction. " +
                                "Red carnations specifically " + "represent admiration, deep love and affection.", 2,
                        "https://drive.google.com/uc?id=1HcSm2Agn3wozMnTZ2911BP8uI3ywvvI_",
                        "https://drive.google.com/uc?id=1_ycrn70eVF3NYHH7qdNsVGFzHcArNx5M",
                        "https://drive.google.com/uc?id=1CqT2Q-dzYEdsd4g94p-OaI7cFUn5wmlC"),
                new Flower("Purple Iris", moneyUtils.amountWithAppCurrency(4.99),
                        "While any iris symbolizes royalty, wisdom and valor, " +
                                "the purple coloured ones represent royalty, wisdom, respect and compliments.", 2,
                        "https://drive.google.com/uc?id=1NS9_OPZB8giCmljHqSKGu7IPBmxGPzD6",
                        "https://drive.google.com/uc?id=1BAQ-X89t888MgDFM13pav1sYMHpy0nVx",
                        "https://drive.google.com/uc?id=15OTWJIvUM9_591kh8rc2Jgx-rPis4JCQ"),
                new Flower("Pink Orchid", moneyUtils.amountWithAppCurrency(4.99),
                        "The pink Orchid has the honour of representing innocence, " +
                                "femininity, grace, joy and happiness. It also represents the celebration " +
                                "of the 14th and 28th wedding anniversary.", 2,
                        "https://drive.google.com/uc?id=1mKbbxCnf5fzFFNW2N5XZ6NxvLqOCgSz_",
                        "https://drive.google.com/uc?id=1ONxFEqD8tGO61U15-UQ7_aGmBXNa6Ixa",
                        "https://drive.google.com/uc?id=1A44FbEEaGzmjE7zURbptizYywxCPjsJ8"),
                new Flower("Pink Gerbera", moneyUtils.amountWithAppCurrency(5.50),
                        "Pink signifies grace and gentility. Gerberas containing mentioned colour" +
                                " additionally express admiration, sympathy and gratitude.", 2,
                        "https://drive.google.com/uc?id=1UBowYPmFcZxgxlaMEWC_qTVqkYGdW_CP",
                        "https://drive.google.com/uc?id=1Lxut8V0PFLSaAsrgwaq-zPHHUbhtY5MY",
                        "https://drive.google.com/uc?id=1-I1skmUuXqh7v0AgzEKGSUiDlzg6w0cR"),
                new Flower("Pink Rose", moneyUtils.amountWithAppCurrency(5.99),
                        "The meaning of pink roses can stand for femininity, elegance, " +
                                "refinement and sweetness", 2,
                        "https://drive.google.com/uc?id=1TqYrZswHeVkFVcVe_GBw51ZB4mVCy5Sh",
                        "https://drive.google.com/uc?id=1LiF2OWQJfGbia1Z3hbbFFWnLZbJKixBL",
                        "https://drive.google.com/uc?id=16HWjspDkuFAfeVjoZxNarwPANyID3ByV"),
        };
    }
    
    private List<Flower> createExemplaryFlowers(int numberOfEntities) {
        /* Create overall entity configuration  */
        String flowerName = "Exemplary Flower";
        Money price = Money.of(5, moneyUtils.getApplicationCurrencyUnit());
        String description = "Exemplary Description";
        int height = 5;
        String imageLarge = "https://drive.google.com/uc?id=1SdbHKr6lGyQERBJce969tfMENR0f28Aw";
        String imageMedium = "https://drive.google.com/uc?id=1Ky4jjG4XWYE4Owk2kQgeNP3ZxNRVxGaR";
        String imageSmall = "https://drive.google.com/uc?id=1anztMez9eFyMiVVWpjT3g4DlixxL5jxP";
        List<Flower> flowerList = new LinkedList<>();

        /* Create and save to database defined number of entities only with changed name */
        for (int i = 0; i < numberOfEntities; i++) {
            flowerList.add(new Flower(flowerName + i, price, description, height,
                    imageLarge, imageMedium, imageSmall));
        }
        return flowerList;
    }

    @Override
    public boolean isOnlyForDebug() {
        return ONLY_FOR_DEBUG;
    }

    public int getNumberOfEntities() {
        return numberOfEntities;
    }

    public FlowerSeeder setNumberOfEntities(int numberOfEntities) {
        this.numberOfEntities = numberOfEntities;
        return this;
    }

    /* Check if value already exists in database */
    private boolean checkExistence(Flower flower) {
        return flowerRepository.findByName(flower.getName()).isPresent();
    }
}
