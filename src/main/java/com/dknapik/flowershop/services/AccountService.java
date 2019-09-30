package com.dknapik.flowershop.services;

import java.security.Principal;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.dknapik.flowershop.viewmodel.account.AccountDetailsViewModel;
import com.dknapik.flowershop.viewmodel.account.AccountViewModel;
import com.dknapik.flowershop.viewmodel.account.PasswordChangeViewModel;
import com.dknapik.flowershop.viewmodel.account.PasswordViewModel;
import com.dknapik.flowershop.SpringContext;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.exceptions.BindingException;
import com.dknapik.flowershop.exceptions.DataProcessingException;
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
	
	public void createNewUser(AccountViewModel accViewModel, BindingResult bindingResult) throws BindingException {
		
		if(bindingResult.hasErrors())
			throw new BindingException(bindingResult.getFieldError().getDefaultMessage(), accViewModel.getClass());
		
		this.accountRepo.saveAndFlush(mapper.map(accViewModel, Account.class));
	}
	
	public Account retrieveAccountDetails(Principal principal) throws DataProcessingException {
		Account acc = this.accountRepo.findByName(principal.getName());//.orElseThrow(
				//() -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
			//);
		acc.setPasswordNoEncoding("");
		return acc;
	}
	
	public void updateAccount(AccountDetailsViewModel accDetailsViewModel, BindingResult bindingResult, Principal principal) throws BindingException, DataProcessingException  {
		
		if(bindingResult.hasErrors())
			throw new BindingException(bindingResult.getFieldError().getDefaultMessage(), accDetailsViewModel.getClass());
		
		Account acc = this.accountRepo.findByName(principal.getName());//.orElseThrow(
				//() -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
			//);
		
		this.mapper.map(accDetailsViewModel, acc);
		
		this.accountRepo.saveAndFlush(acc);
	}
	
	public void updatePassword(PasswordChangeViewModel passwordChangeViewModel, BindingResult bindingResult, Principal principal) throws BindingException, DataProcessingException {
	
		if(bindingResult.hasErrors())
			throw new BindingException(bindingResult.getFieldError().getDefaultMessage(), passwordChangeViewModel.getClass());
		
		Account acc = this.accountRepo.findByName(principal.getName());//.orElseThrow(
					//() -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
				//);
		
		PasswordEncoder encoder = SpringContext.getBean(PasswordEncoder.class);
		
		if (encoder.matches(passwordChangeViewModel.getCurrentPassword(), acc.getPassword()) 
				&& passwordChangeViewModel.getNewPassword().contentEquals(passwordChangeViewModel.getNewPasswordConfirmation()) ) {
			
			acc.setPassword(passwordChangeViewModel.getNewPassword());
			this.accountRepo.saveAndFlush(acc);
			
		} else {
			throw new DataProcessingException("Provided password doesn't match actual password");
		}
	}
	
	public void deleteAccount(String password, BindingResult bindingResult, Principal principal) throws DataProcessingException {
		
		
		Account acc = this.accountRepo.findByName(principal.getName());//.orElseThrow(
				//() -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
			//);
		
		PasswordEncoder encoder = SpringContext.getBean(PasswordEncoder.class);
		if(encoder.matches(password, acc.getPassword())) {
			this.accountRepo.delete(acc);
		} else {
			throw new DataProcessingException("Provided password doesn't match");
		}
		
	}
}
