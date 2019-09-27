package com.dknapik.flowershop.api;


import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dknapik.flowershop.viewmodel.account.AccountDetailsViewModel;
import com.dknapik.flowershop.viewmodel.account.AccountViewModel;
import com.dknapik.flowershop.viewmodel.account.PasswordChangeViewModel;
import com.dknapik.flowershop.viewmodel.account.PasswordViewModel;
import com.dknapik.flowershop.exceptions.BindingException;
import com.dknapik.flowershop.exceptions.DataProcessingException;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.services.AccountService;

@RestController
@RequestMapping("/account")
@CrossOrigin
public class AccountController {

	protected final Logger log = LogManager.getLogger(getClass().getName()); 
	private final AccountService service;
	
	@Autowired
	public AccountController(AccountService service) {	
		this.service = service;
	}

	@PostMapping("/register")
	public ResponseEntity<String> createAccount(@Valid @RequestBody AccountViewModel accountViewModel, BindingResult bindingResult) {
		String returnMsg = "Account created succesfully !";
		HttpStatus status = HttpStatus.OK;
		
		try {
			this.service.createNewUser(accountViewModel, bindingResult);
		} catch (BindingException e) {
			log.warn("Exception creating new account", e);
			returnMsg = e.getMessage();
			status = e.getHttpStatus();
		}
		return new ResponseEntity<>(returnMsg, status);
	}
	
	@GetMapping()
	public ResponseEntity<Account> retrieveAccount(@RequestParam String accountID) {
		Account acc = null;
		HttpStatus status = HttpStatus.OK;
			try {
				acc = this.service.retrieveAccountDetails(accountID);
			} catch (DataProcessingException e) {
				log.warn("Exception retrieving account data", e);
				status = e.getHttpStatus();
			}
		
		return new ResponseEntity<>(acc, status);
	}
	
	@PutMapping()
	public ResponseEntity<String> updateAccount(@Valid @RequestBody AccountDetailsViewModel accountDetailsViewModel, BindingResult bindingResult) {
		String returnMsg = "Account details updated succesfully !";
		HttpStatus status = HttpStatus.OK;
		
		try {
			this.service.updateAccount(accountDetailsViewModel, bindingResult);
		} catch (BindingException | DataProcessingException e) {
			log.warn("Exception updating account data", e);
			returnMsg = e.getMessage();
			status = e.getHttpStatus();
		}

		return new ResponseEntity<>(returnMsg, status);
	}
	
	@PutMapping("/password")
	public ResponseEntity<String> updatePassword(@Valid @RequestBody PasswordChangeViewModel passwordChangeViewModel, BindingResult bindingResult) {
		String returnMsg = "Password updated succesfully !";
		HttpStatus status = HttpStatus.OK;
		
		try {
			this.service.updatePassword(passwordChangeViewModel, bindingResult);
		} catch (BindingException | DataProcessingException e) {
			this.log.warn("Exception changing password", e);
			returnMsg = e.getMessage();
			status = e.getHttpStatus();
		}
		
		return new ResponseEntity<>(returnMsg, status);
	}
	
	//TODO add DTO to confirm that user deleting account is an actual user
	@DeleteMapping()
	public ResponseEntity<String> deleteAccount(@Valid @RequestBody PasswordViewModel passwordViewModel, BindingResult bindingResult) {
		String message = "Account deleted succesfully !";
		HttpStatus status =  HttpStatus.OK;
		
			try {
				this.service.deleteAccount(passwordViewModel, bindingResult);
			} catch (DataProcessingException e) {
				this.log.warn("Exception deleting account", e);
				message = e.getMessage();
				status = e.getHttpStatus();
			}

		return new ResponseEntity<>(message, status);
	}
	

}
