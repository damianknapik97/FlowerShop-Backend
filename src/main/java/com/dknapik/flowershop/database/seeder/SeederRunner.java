package com.dknapik.flowershop.database.seeder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * 
 * Class used for initializing some values inside database used by application.
 * Regardless of debug mode, this application creates root users with admin privileges
 * that allows later client to configure application on its own.
 * 
 * @author Damian
 *
 */
@Component
public class SeederRunner implements CommandLineRunner {
	@Value("${app-debug-mode}")
	private boolean debugMode;
	private List<SeederInt> seederList;

	/**
	 *  Initialize all sub seeder classes
	 */
	public SeederRunner(AccountSeeder accountSeeder,
						AddonSeeder addonSeeder,
						FlowerSeeder flowerSeeder) {

		seederList = Arrays.asList(
				accountSeeder,
				addonSeeder,
				flowerSeeder.setNumberOfEntities(30)
		);
	}

	/**
	 *  Seed database with data
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
