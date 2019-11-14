package com.dknapik.flowershop.database;

import java.util.*;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import com.dknapik.flowershop.model.*;
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
		this.context = context;
		this.currency = Monetary.getCurrency(env.getProperty("app-monetary-currency"));
	}
	
	@Override
	public void run(String... args) throws Exception {
		this.initializeAccounts();
		if (debugMode) {
			this.initializeFlowers();
			//this.initializeFlowerPacks();
			this.initializeBouquets();
			//this.initializeShoppingCart();
		}
	}
	
	/**
	 * Create basic user accounts
	 * root account is always initialized
	 */
	private void initializeAccounts() {
		List<Account> initialDataAccounts = new LinkedList<>();
		
		PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
		
		if (!this.accountRepository.findByName("root").isPresent()) {
			initialDataAccounts.add(new Account("root", encoder.encode("root"), "root@test.pl", UserRoles.ADMIN));
		}
		
		if (debugMode) {
			initialDataAccounts.add(new Account("employee", encoder.encode("employee"), "employee@test.pl", UserRoles.EMPLOYEE));
			initialDataAccounts.add(new Account("user", encoder.encode("user"), "user@test.pl", UserRoles.USER));	
		}

		this.accountRepository.saveAll(initialDataAccounts);
	}

	/**
	 * Initialize database with flowers, used for debugging/testing purposes
	 */
	private void initializeFlowers() {
		List<Flower> initialDataFlowers = new ArrayList<>();

		initialDataFlowers.add(new Flower("Róża", Money.of(5.00, currency)));
		initialDataFlowers.add(new Flower("Tulipan", Money.of(4.00, currency)));
		initialDataFlowers.add(new Flower("Frezja", Money.of(6.00, currency)));

		for(int i = 0; i < 15; i++) {
			initialDataFlowers.add(new Flower("TestFlower" + i, Money.of(5.00, currency)));
		}

		flowerRepository.saveAll(initialDataFlowers);
	}

	private void initializeFlowerPacks() {
		String flowerName = "Tulipan";
		Flower flower = this.flowerRepository.findByName(flowerName)
				.orElseThrow(() -> new DataRetrievalFailureException("Flower with name " + flowerName + " not found !"));
		this.flowerPackRepository.saveAndFlush(new FlowerPack(flower, 5));
	}

	/**
	 * Initialize database with Bouquets consisting of initialized flowers, used for debugging/testing purposes
	 */
	private void initializeBouquets() {
		String flowerNameRose = "Róża";
		String flowerNameTulip = "Tulipan";
		Set<FlowerPack> flowerPackList = new HashSet<>();
		List<Bouquet> bouquetList = new LinkedList<>();
		Flower flowerRose = this.flowerRepository.findByName(flowerNameRose)
				.orElseThrow(() -> new DataRetrievalFailureException("Flower with name " + flowerNameRose + " not found !"));
		Flower flowerTulip = flowerRepository.findByName(flowerNameTulip)
				.orElseThrow(() -> new DataRetrievalFailureException("Flower with name " + flowerNameTulip + " not found !"));

		flowerPackList.add(new FlowerPack(flowerRose, 5));
		bouquetList.add(new Bouquet("Basic", Money.of(10, currency), 10, new HashSet<>(flowerPackList)));

		flowerPackList.add(new FlowerPack(flowerTulip, 3));
		bouquetList.add(new Bouquet("Expanded", Money.of(15, currency), 15, flowerPackList));

		bouquetRepository.saveAll(bouquetList);
		bouquetRepository.flush();

	}

	//TODO What if there are two FlowerPacks with the same flower but different quantities ???
	private void initializeShoppingCart() {
		String shoppingCartName = "TestCart";
		String userName = "root";
		String bouquetName = "Basic";
		String flowerName = "Tulipan";
		Account account = this.accountRepository.findByName(userName)
				.orElseThrow(() -> new UsernameNotFoundException(
						"User with name " + userName + " not found"));
		Bouquet bouquet = this.bouquetRepository.findByName(bouquetName)
				.orElseThrow(() -> new DataRetrievalFailureException(
						"Bouquet with name " + bouquetName + " not found !"));
		Flower flower = this.flowerRepository.findByName(flowerName)
				.orElseThrow(() -> new DataRetrievalFailureException(
						"Flower with name " + flowerName + " not found !"));
		FlowerPack flowerPack = this.flowerPackRepository.findByFlower(flower)
				.orElseThrow(() -> new DataRetrievalFailureException(
						"FlowerPack containing Flower with name " + flower.getName() + " not found !"));

		// TODO finish...

	}
	
}
