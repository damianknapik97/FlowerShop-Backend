package com.dknapik.flowershop.database.seeder;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * TODO This module should be removed as Seeders not only decrease performance of the tests
 * by being initialized every time, they also are inappropriately/messy implemented.
 *
 * Current idea: Create manually PostgreSQL database, and reuse it using override of docker volumes.
 */

/**
 * Class used for initializing some values inside database used by application.
 * Regardless of debug mode, this application creates root users with admin privileges
 * that allows later client to configure application on its own.
 *
 * @author Damian
 */
@Component
@ToString
public class SeederRunner implements CommandLineRunner {
    @Value("${app-debug-mode}")
    private boolean debugMode;
    private List<SeederInt> seederList;

    /**
     * Initialize all sub seeder classes
     */
    public SeederRunner(AccountSeeder accountSeeder,
                        AddonSeeder addonSeeder,
                        FlowerSeeder flowerSeeder,
                        SouvenirSeeder souvenirSeeder,
                        OccasionalArticleSeeder occasionalArticle,
                        ShoppingCartSeeder shoppingCartSeeder,
                        OccasionalArticleSeeder occasionalArticleSeeder) {

        seederList = Arrays.asList(
                accountSeeder,
                addonSeeder,
                souvenirSeeder,
                flowerSeeder.setNumberOfEntities(52),
                occasionalArticle,
                shoppingCartSeeder,
                occasionalArticleSeeder
        );
    }

    /**
     * Seed database with data
     */
    @Override
    public void run(String... args) {
        for (SeederInt seeder : seederList) {
            if (seeder.isOnlyForDebug() || debugMode) {
                seeder.seed();
            }
        }
    }
}
