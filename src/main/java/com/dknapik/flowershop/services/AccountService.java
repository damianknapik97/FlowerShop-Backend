package com.dknapik.flowershop.services;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.dknapik.flowershop.api.viewmodel.AccountViewModel;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.model.Account;

@Service
public class AccountService {
	
	protected final Logger log = LogManager.getLogger(getClass().getName()); 
	private final ModelMapper mapper;
	private final AccountRepository accountRepo;
	
	@Autowired
	public AccountService(AccountRepository accountRepo) {
		this.accountRepo = accountRepo;
		this.mapper = new ModelMapper();
	}
	
	public void createNewUser(AccountViewModel accViewModel) {
		this.accountRepo.saveAndFlush(mapper.map(accViewModel, Account.class));
	}
	
	public Account retrieveAccountDetails(String id) throws IOException {
		UUID accId = UUID.fromString(id);	
		Account acc = accountRepo.findById(accId).orElseThrow(() -> new IOException("Couldn't map provided ID with any account from database"));
		acc.setPasswordNoEncoding("");
		return acc;
	}
	
	public void updateAccount(AccountViewModel accViewModel, BindingResult bindingResult) {
		Account acc = this.accountRepo.findByName(accViewModel.getName());
		UUID id = acc.getId();
		//This means that password is empty, no changes to password required
		if(bindingResult.hasErrors() && bindingResult.getErrorCount() == bindingResult.getFieldErrorCount("password")) {
			
			String password = acc.getPassword();
			acc = this.mapper.map(accViewModel, Account.class);
			acc.setId(id);
			acc.setPasswordNoEncoding(password);
			
			this.accountRepo.saveAndFlush(acc);
		} else {
			acc = this.mapper.map(accViewModel, Account.class);
			acc.setId(id);
			this.accountRepo.saveAndFlush(acc);
		}
	}
	
	public void deleteAccount(String accountID) throws IOException {
		UUID accId = UUID.fromString(accountID);	
		Account acc = accountRepo.findById(accId).orElseThrow(() -> new IOException("Couldn't map provided ID with any account from database"));
		this.accountRepo.delete(acc);
	}
	

}
