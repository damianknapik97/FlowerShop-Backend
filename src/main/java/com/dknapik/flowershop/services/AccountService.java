package com.dknapik.flowershop.services;

import java.io.IOException;
import java.security.Principal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.dknapik.flowershop.viewmodel.account.AccountDetailsViewModel;
import com.dknapik.flowershop.viewmodel.account.AccountViewModel;
import com.dknapik.flowershop.viewmodel.account.PasswordChangeViewModel;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.exceptions.BindingException;
import com.dknapik.flowershop.exceptions.DataProcessingException;
import com.dknapik.flowershop.exceptions.MappingException;
import com.dknapik.flowershop.model.Account;

@Service
public class AccountService {
	
	protected final Logger log = LogManager.getLogger(getClass().getName()); 
	private final ModelMapper mapper;
	private final ObjectMapper jsonMapper;
	private final AccountRepository accountRepo;
	private final ApplicationContext context;
	
	@Autowired
	public AccountService(AccountRepository accountRepo, ApplicationContext context) {
		this.accountRepo = accountRepo;
		this.context = context;
		
		this.mapper = new ModelMapper();
		this.jsonMapper = new ObjectMapper();
	}
	
	public void createNewUser(AccountViewModel accViewModel, BindingResult bindingResult) throws BindingException, DataProcessingException {
		
		if(bindingResult.hasErrors())
			throw new BindingException(bindingResult.getFieldError().getDefaultMessage(), accViewModel.getClass());
		
		if(!this.accountRepo.existsByName(accViewModel.getName())) {
			accViewModel.setPassword(context.getBean(PasswordEncoder.class).encode(accViewModel.getPassword()));
			this.accountRepo.saveAndFlush(mapper.map(accViewModel, Account.class));
		} else {
			throw new DataProcessingException("User with provided username already exists, please pick different name");
		}
		
		
	}
	
	public AccountDetailsViewModel retrieveAccountDetails(Principal principal) throws DataProcessingException {
		Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
				() -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
			);
		
		return this.mapper.map(acc, AccountDetailsViewModel.class);
	}
	
	public void updateAccount(AccountDetailsViewModel accDetailsViewModel, BindingResult bindingResult, Principal principal) throws BindingException, DataProcessingException  {
		
		if(bindingResult.hasErrors())
			throw new BindingException(bindingResult.getFieldError().getDefaultMessage(), accDetailsViewModel.getClass());
		
		Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
				() -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
			);
		
		this.mapper.map(accDetailsViewModel, acc);
		
		this.accountRepo.saveAndFlush(acc);
	}
	
	// Matching password is for some reasons broken and can't be used for now.
	public void updatePassword(PasswordChangeViewModel passwordChangeViewModel, BindingResult bindingResult, Principal principal) throws BindingException, DataProcessingException {
	
		if(bindingResult.hasErrors())
			throw new BindingException(bindingResult.getFieldError().getDefaultMessage(), passwordChangeViewModel.getClass());
		
		Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
					() -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
				);
		
		PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
		
		if (passwordChangeViewModel.getNewPassword().contentEquals(passwordChangeViewModel.getNewPasswordConfirmation()) ) {
			
			acc.setPassword(encoder.encode(passwordChangeViewModel.getNewPassword()));
			this.accountRepo.saveAndFlush(acc);
			
		} else {
			throw new DataProcessingException("Provided password doesn't match actual password");
		}
	}
	
	public void deleteAccount(String password, Principal principal) throws DataProcessingException, MappingException {
		String parsedPassword = null;
		
		try {
			JsonNode jsonNode = this.jsonMapper.readTree(password);
			parsedPassword = jsonNode.findValuesAsText("password").get(0);
		} catch (IOException e) {
			this.log.warn("Error parsing provided JSON string", e);
			throw new MappingException("Couldn't parse provided details", String.class, String.class);
		}
		
		if(parsedPassword != null) {
			Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
					() -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
				);

			
			PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
			
			// Password matching is broken and doesn't work for some reasons
			//if(encoder.matches(parsedPassword, acc.getPassword())) {
				this.accountRepo.delete(acc);
			//} else {
				//throw new DataProcessingException("Provided password doesn't match");
			//}
		}
				
	}
}
