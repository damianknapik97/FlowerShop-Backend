package com.dknapik.flowershop.database;

import java.util.*;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import com.dknapik.flowershop.database.product.AddonRepository;
import com.dknapik.flowershop.database.product.FlowerRepository;
import com.dknapik.flowershop.model.*;
import com.dknapik.flowershop.model.product.Addon;
import com.dknapik.flowershop.model.product.Flower;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dknapik.flowershop.security.UserRoles;

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
public class DatabaseSeeder implements CommandLineRunner {
	@Value("${app-debug-mode}")
	private boolean debugMode;
	private final ApplicationContext context;
	private final CurrencyUnit currency;

	private final AccountRepository accountRepository;
	private final FlowerRepository flowerRepository;
	private final AddonRepository addonRepository;


	@Autowired
	public DatabaseSeeder(Environment env,
						  ApplicationContext context,
						  AccountRepository accountRepository,
						  FlowerRepository flowerRepository,
						  AddonRepository addonRepository) {
		this.currency = Monetary.getCurrency(env.getProperty("app-monetary-currency"));
		this.context = context;
		this.accountRepository = accountRepository;
		this.flowerRepository = flowerRepository;
		this.addonRepository = addonRepository;
	}
	
	@Override
	public void run(String... args) throws Exception {
		int numberOfObjects = 30;

		this.initializeAccounts();
		if (debugMode) {
			initializeFlowers(numberOfObjects);
		}
	}
	
	/**
	 * Create basic user accounts
	 * root account is always initialized
	 */
	private void initializeAccounts() {
		List<Account> initialDataAccounts = new LinkedList<>();
		
		PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
		
		if (!this.accountRepository.existsByName("root")) {
			Account rootAcc = new Account("root", encoder.encode("root"), "root@test.pl", UserRoles.ADMIN);
			initialDataAccounts.add(rootAcc);
		}
		
		if (debugMode) {
			initialDataAccounts.add(new Account("employee", encoder.encode("employee"), "employee@test.pl", UserRoles.EMPLOYEE));
			initialDataAccounts.add(new Account("user", encoder.encode("user"), "user@test.pl", UserRoles.USER));	
		}

		this.accountRepository.saveAll(initialDataAccounts);
		this.accountRepository.flush();
	}

	private void initializeFlowers(int numberOfObjects) {
		String flowerName = "Rose" ;
		Money price = Money.of(5, currency);
		String description = "Long a symbol of love and passion, the ancient Greeks and Romans associated roses with" +
				" Aphrodite and Venus, goddess of love. Used for hundreds of years to convey messages without words," +
				" they also represent confidentiality. In fact, the Latin expression \"sub rosa\"(literally," +
				" \"under the rose\") means something told in secret, and in ancient Rome, a wild rose was placed" +
				" on the door to a room where confidential matters were being discussed.";
		int height = 5;
		Flower newFlower;


		for (int i = 0; i < numberOfObjects; i++) {
			newFlower = new Flower(flowerName + i, price, description, height);
			flowerRepository.save(newFlower);
		}
		flowerRepository.flush();
	}

	private void initializeAddons() {
		Money price = Money.of(2, currency);
		String description = "The blue ribbon is a symbol of high quality.";
		Addon[] addonArray = {
				new Addon("Ribbon", "Blue", price, description),
				new Addon("Ribbon", "Red", price, description),
				new Addon("Bow", "Blue", price, description),
		};

		addonRepository.saveAll(Arrays.asList(addonArray));
		addonRepository.flush();
	}


}
