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
	private final AccountRepository accountRepository;

	private final ApplicationContext context;
	private final CurrencyUnit currency;

	private final String flowerNameRose = "Róża";
	private final String flowerNameTulip = "Tulipan";
	private final String flowerNameFreesia = "Frezja";

	@Autowired
	public DatabaseSeeder(AccountRepository accountRepository,
						  Environment env,
						  ApplicationContext context) {
		this.accountRepository = accountRepository;
		this.currency = Monetary.getCurrency(env.getProperty("app-monetary-currency"));
		this.context = context;
	}
	
	@Override
	public void run(String... args) throws Exception {
		this.initializeAccounts();
		if (debugMode) {
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


}
