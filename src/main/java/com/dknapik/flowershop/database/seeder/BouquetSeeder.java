package com.dknapik.flowershop.database.seeder;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.database.bouquet.BouquetRepository;
import com.dknapik.flowershop.database.product.AddonRepository;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.model.bouquet.Bouquet;
import com.dknapik.flowershop.model.bouquet.BouquetAddon;
import com.dknapik.flowershop.model.bouquet.BouquetFlower;
import com.dknapik.flowershop.model.product.Addon;
import com.dknapik.flowershop.model.product.AddonColour;
import com.dknapik.flowershop.model.product.Flower;
import com.dknapik.flowershop.utils.MoneyUtils;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@ToString
@Component
public class BouquetSeeder implements SeederInt {
    private MoneyUtils moneyUtils;
    private AccountRepository accountRepository;
    private FlowerRepository flowerRepository;
    private AddonRepository addonRepository;
    private BouquetRepository bouquetRepository;

    @Autowired
    public BouquetSeeder(MoneyUtils moneyUtils,
                         AccountRepository accountRepository,
                         FlowerRepository flowerRepository,
                         AddonRepository addonRepository,
                         BouquetRepository bouquetRepository) {
        this.moneyUtils = moneyUtils;
        this.accountRepository = accountRepository;
        this.flowerRepository = flowerRepository;
        this.addonRepository = addonRepository;
        this.bouquetRepository = bouquetRepository;
    }

    @Override
    public void seed() {
        List<Flower> flowerList = createPredefinedFlowers();
        List<Addon> addonList = createPredefinedAddons();
        /* TODO: This looks terrible, unfortunately no other idea at the moment */
        createSmallExemplaryBouquet(flowerList.get(0), flowerList.get(1), addonList.get(0));
        createMediumExemplaryBouquet(flowerList.get(0), flowerList.get(1), flowerList.get(2),
                addonList.get(0), addonList.get(1));


    }

    private void createSmallExemplaryBouquet(Flower flower1, Flower flower2, Addon addon1) {
        List<BouquetFlower> bouquetFlowerContent = new LinkedList<>();
        bouquetFlowerContent.add(new BouquetFlower(4, flower1));
        bouquetFlowerContent.add(new BouquetFlower(5, flower2));

        List<BouquetAddon> bouquetAddonContent = new LinkedList<>();
        bouquetAddonContent.add(new BouquetAddon(addon1));

        Bouquet bouquet = new Bouquet("Small Exemplary",
                moneyUtils.amountWithAppCurrency(5.00),
                5,
                bouquetFlowerContent,
                bouquetAddonContent,
                "https://drive.google.com/uc?id=1HeZeyYTQtOFqG7UlDfD9kByWlH3xSDqB",
                "https://drive.google.com/uc?id=1sdhPZhsicTaA5RRyrpuAS9GNnokF1_PQ",
                "https://drive.google.com/uc?id=1kk8ZDp-QnJVL3zcwkgYGHHBOuHgmVuWH",
                false);

        if (!bouquetRepository.existsByNameAndDiscountPercentageAndUserCreated(bouquet.getName(),
                bouquet.getDiscountPercentage(), bouquet.isUserCreated())) {
            bouquetRepository.saveAndFlush(bouquet);
        }
    }

    private void createMediumExemplaryBouquet(Flower flower1, Flower flower2, Flower flower3,
                                              Addon addon1, Addon addon2) {
        List<BouquetFlower> bouquetFlowerContent = new LinkedList<>();
        bouquetFlowerContent.add(new BouquetFlower(4, flower1));
        bouquetFlowerContent.add(new BouquetFlower(5, flower2));
        bouquetFlowerContent.add(new BouquetFlower(3, flower3));

        List<BouquetAddon> bouquetAddonContent = new LinkedList<>();
        bouquetAddonContent.add(new BouquetAddon(addon1));
        bouquetAddonContent.add(new BouquetAddon(addon2));

        Bouquet bouquet = new Bouquet("Medium Exemplary",
                moneyUtils.amountWithAppCurrency(7.00),
                5,
                bouquetFlowerContent,
                bouquetAddonContent,
                "https://drive.google.com/uc?id=15oxOoV-UD5nNsIOI7kuUjG8f8qHMaOtX",
                "https://drive.google.com/uc?id=1gTAav_7xbN-548rpL12RAPYOr9bFCe6w",
                "https://drive.google.com/uc?id=17t_rDgAu1KwVnL48pvNm1TYrAkGfXLRK",
                false);

        if (!bouquetRepository.existsByNameAndDiscountPercentageAndUserCreated(bouquet.getName(),
                bouquet.getDiscountPercentage(), bouquet.isUserCreated())) {
            bouquetRepository.saveAndFlush(bouquet);
        }
    }

    /**
     * Creates predefined Flower entities, seeds the database with them and returns their list.
     *
     * @return List with saved Addon entities.
     */
    private List<Flower> createPredefinedFlowers() {
        List<Flower> flowerList = new LinkedList<>();

        flowerList.add(new Flower("Exemplary Bouquet Flower 1",
                moneyUtils.amountWithAppCurrency(4.99),
                "Exemplary Flower",
                5,
                "https://drive.google.com/uc?id=1Q6aRM2DUJ1qVPMmCk1kv8v2Q7e9TFNOX",
                "https://drive.google.com/uc?id=1JsKMAjt-HQcy24WqnBg7dKQUa8Wpvmgl",
                "https://drive.google.com/uc?id=1Nco-e6v-ei3DrWRYNAXC6xsEE9i1Ek4G"));
        flowerList.add(new Flower("Exemplary Bouquet Flower 2",
                moneyUtils.amountWithAppCurrency(4.99),
                "Exemplary Flower",
                5,
                "https://drive.google.com/uc?id=10AvfqidANER6oIIWi3IEPSzPwDwA2rZv",
                "https://drive.google.com/uc?id=1Xu7zesN_wSAK9cgsdBU2TWhWpsM35RO7",
                "https://drive.google.com/uc?id=1O-9IFRKfSqfYqTiM5a2w6QBisNnP1zRa"));
        flowerList.add(new Flower("Exemplary Bouquet Flower 3",
                moneyUtils.amountWithAppCurrency(4.99),
                "Exemplary Flower",
                5,
                "https://drive.google.com/uc?id=1n4KQf5OTiFvdkWqj3W-MteanYzpF2vTN",
                "https://drive.google.com/uc?id=15MlUb0ezTZGPzbGSzsDuh4CuXFNOxCQx",
                "https://drive.google.com/uc?id=14w7XLYlWziFs4o1WR1yDfUThi52netGw"));

        for (Flower flower : flowerList) {
            if (!flowerRepository.existsByNameAndHeightAndDescription(flower.getName(),
                    flower.getHeight(), flower.getDescription())) {
                flowerRepository.save(flower);
            }
        }
        flowerRepository.flush();
        return flowerList;
    }

    /**
     * Creates predefined Addon entities, seeds the database with them and returns their list.
     *
     * @return List with saved Addon entities.
     */
    private List<Addon> createPredefinedAddons() {
        List<Addon> addonList = new LinkedList<>();
        addonList.add(new Addon("Ribbon",
                AddonColour.PINK, moneyUtils.amountWithAppCurrency(1.99),
                "Beautiful ornament",
                "https://drive.google.com/uc?id=1G9w7ha-HN3TaOUmZs6IRkQ-J7Ygay1aK",
                "https://drive.google.com/uc?id=1gB0ZZdwNt3O2VKEA_-rwIjMAC_JMU1GB",
                "https://drive.google.com/uc?id=1aWdglA0iRziql21xYbxKMydc8oPW055S"));
        addonList.add(new Addon("Ribbon",
                AddonColour.RED, moneyUtils.amountWithAppCurrency(1.99),
                "Beautiful ornament",
                "https://drive.google.com/uc?id=1MASj9UzsWTqi1sz5OZDQF5Yyr8dfpSHr",
                "https://drive.google.com/uc?id=1FiAsIPYgtxBAX-SKMyClTODXXx7E0mM7",
                "https://drive.google.com/uc?id=1ak6beZYB5MKTRQn8A2iPsgpurl_trHbu"));

        for (Addon addon : addonList) {
            if (!addonRepository.existsByNameAndColourAndDescription(addon.getName(),
                    addon.getColour(), addon.getDescription())) {
                addonRepository.save(addon);
            }
        }
        addonRepository.flush();

        return addonList;
    }

    @Override
    public boolean isOnlyForDebug() {
        return true;
    }
}
