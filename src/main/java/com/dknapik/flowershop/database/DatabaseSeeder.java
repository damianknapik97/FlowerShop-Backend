package com.dknapik.flowershop.database;

import java.util.*;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import com.dknapik.flowershop.model.*;
import com.dknapik.flowershop.services.FlowerPackService;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dknapik.flowershop.security.UserRoles;
import org.springframework.transaction.annotation.Transactional;

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
	private final BouquetRepository bouquetRepository;
	private final FlowerRepository flowerRepository;
	private final FlowerPackRepository flowerPackRepository;
	private final AccountRepository accountRepository;

	private final ShoppingCartRepository shoppingCartRepository;
	private final ApplicationContext context;
	private final CurrencyUnit currency;


	private final String flowerNameRose = "Róża";
	private final String flowerNameTulip = "Tulipan";
	private final String flowerNameFreesia = "Frezja";

	@Autowired
	public DatabaseSeeder(BouquetRepository bouquetRepository,
						  FlowerRepository flowerRepository,
						  FlowerPackRepository flowerPackRepository,
						  AccountRepository accountRepository,
						  ShoppingCartRepository shoppingCartRepository,
						  Environment env,
						  ApplicationContext context) {
		this.bouquetRepository = bouquetRepository;
		this.flowerRepository = flowerRepository;
		this.flowerPackRepository = flowerPackRepository;
		this.accountRepository = accountRepository;
		this.shoppingCartRepository = shoppingCartRepository;
		this.currency = Monetary.getCurrency(env.getProperty("app-monetary-currency"));
		this.context = context;
	}
	
	@Override
	public void run(String... args) throws Exception {
		this.initializeAccounts();
		if (debugMode) {
			this.initializeFlowers();
			this.initializeFlowerPacks();
			this.initializeBouquets();
			this.initializeShoppingCart();
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

	/**
	 * Initialize database with flowers, used for debugging/testing purposes
	 */
	private void initializeFlowers() {
		List<Flower> initialDataFlowers = new ArrayList<>();

		initialDataFlowers.add(new Flower(this.flowerNameRose, Money.of(5.00, currency)));
		initialDataFlowers.add(new Flower(this.flowerNameTulip, Money.of(4.00, currency)));
		initialDataFlowers.add(new Flower(this.flowerNameFreesia, Money.of(6.00, currency)));

		for (int i = 0; i < 30; i++) {
			initialDataFlowers.add(new Flower("TestFlower" + i, Money.of(5.00, currency)));
		}

		flowerRepository.saveAll(initialDataFlowers);
		flowerRepository.flush();
	}

	/**
	 * Initialize database with FlowerPacks consisting of flowers and number of flowers in the pack, used for
	 * debugging/testing purposes
	 *
	 */
	private void initializeFlowerPacks() {
		/* Retrieve flowers for FlowerPack construct */
		Flower flowerTulip = this.flowerRepository.findByName(flowerNameTulip)
				.orElseThrow(() -> new DataRetrievalFailureException("Flower with name " + flowerNameTulip + " not found !"));
		Flower flowerFressia = this.flowerRepository.findByName(flowerNameFreesia)
				.orElseThrow(() -> new DataRetrievalFailureException("Flower with name " + flowerNameFreesia + " not found !"));

		/* Convert to list and save only non existing */
		List<FlowerPack> toSave = Arrays.asList(
				new FlowerPack(flowerTulip, 8),
				new FlowerPack(flowerFressia, 8)
		);

		/* Save entities */
		this.flowerPackRepository.saveAll(toSave);
		this.flowerPackRepository.flush();
	}

	/**
	 * Initialize database with Bouquets consisting of initialized flowers, used for debugging/testing purposes
	 */
	private void initializeBouquets() {
		Set<FlowerPack> flowerPackList = new HashSet<>();

		/* Retrieve flowers to constructor bouquets from */
		Flower flowerRose = this.flowerRepository.findByName(flowerNameRose)
				.orElseThrow(() -> new DataRetrievalFailureException("Flower with name " + flowerNameRose + " not found !"));
		Flower flowerTulip = flowerRepository.findByName(flowerNameTulip)
				.orElseThrow(() -> new DataRetrievalFailureException("Flower with name " + flowerNameTulip + " not found !"));

		/* New bouquet from FlowerPack */
		flowerPackList.add(new FlowerPack(flowerRose, 5));
		this.flowerPackRepository.saveAll(flowerPackList);
		this.flowerPackRepository.flush();
		this.bouquetRepository.saveAndFlush(new Bouquet("Basic", Money.of(10, currency), 10, flowerPackList));

		/* New bouquet from two FlowerPacks */
		flowerPackList = new HashSet<>();
		flowerPackList.add(new FlowerPack(flowerRose, 5));
		flowerPackList.add(new FlowerPack(flowerTulip, 3));
		/* Save only non existing FlowerPacks because of existing constraints */
		this.flowerPackRepository.saveAll(flowerPackList);
		//this.flowerPackRepository.flush();
		this.bouquetRepository.saveAndFlush(new Bouquet("Expanded", Money.of(15, currency), 15, flowerPackList));

	}
	/**
	 * Initialize database with shopping cart assigned to root, used for debugging/testing purposes
	 */
	private void initializeShoppingCart() {
		String shoppingCartName = "TestCart";						// New entity name
		String bouquetName = "Basic";								// For retrieving shopping cart product - Bouquet
		int numberOfFlowers = 5;									// For retrieving shopping cart product - FlowerPack
		List<Bouquet> bouquetList;									// New entity bouquet list
		List<FlowerPack> flowerPackList = new LinkedList<>();		// New entity flower list

		/* Retrieve bouquet list for entity constructor */
		bouquetList = this.bouquetRepository.findByNameAndFetchFlowerPacksEagerly(bouquetName)
				.orElseThrow(() -> new DataRetrievalFailureException(
						"Bouquet with name " + bouquetName + " not found !"));

		/* Retrieve FlowerPack and create list from it for entity constructor */
		FlowerPack flowerPack = this.flowerPackRepository.findByFlower_NameAndNumberOfFlowers(
				flowerNameTulip,
				numberOfFlowers ).orElseGet(() ->{
					FlowerPack toReturn = new FlowerPack(this.flowerRepository.findByName(this.flowerNameTulip).get(), numberOfFlowers);
					this.flowerPackRepository.saveAndFlush(toReturn);
					return toReturn;
		});
		flowerPackList.add(flowerPack);

		this.bouquetRepository.saveAll(bouquetList);
		this.bouquetRepository.flush();

		/* Create and save new shopping cart */
		ShoppingCart shoppingCart = new ShoppingCart(
				shoppingCartName, bouquetList, flowerPackList);

		this.flowerPackRepository.saveAll(flowerPackList);
		this.flowerPackRepository.flush();

		this.bouquetRepository.saveAll(bouquetList);
		this.bouquetRepository.flush();

		this.shoppingCartRepository.saveAndFlush(shoppingCart);
	}
}
