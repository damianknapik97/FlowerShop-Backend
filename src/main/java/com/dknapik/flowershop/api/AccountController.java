package com.dknapik.flowershop.api;



import java.util.HashMap;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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

import com.dknapik.flowershop.api.viewmodel.AccountViewModel;
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
		String responseMsg = "Account created succesfully";
		HttpStatus status = HttpStatus.OK;
		
		if(bindingResult.hasErrors()) {
			log.error("Couldn't map provided credentials with AccountViewModel");
			responseMsg = "Couldn't create account with provided credentials";
			status = HttpStatus.BAD_REQUEST;
		}
		
		this.service.createNewUser(accountViewModel);
		
		return new ResponseEntity<>(responseMsg, status);
	}
	
	@GetMapping()
	public ResponseEntity<Account> retrieveAccount(@RequestParam String accountID) {
		Account acc = null;
		HttpStatus status = HttpStatus.OK;
		try {
			acc = this.service.retrieveAccountDetails(accountID);
		} catch(Exception e) {
			log.error(e.getMessage());
			status = HttpStatus.NOT_FOUND;
		}
		
		return new ResponseEntity<>(acc, status);
	}
	
	//TODO separate DTO needed for password update to confirm that updating user is an actual user
	@PutMapping()
	public ResponseEntity<String> updateAccount(@Valid @RequestBody AccountViewModel accountViewModel, BindingResult bindingResult) {
		String message = "Update Succesfull !";
		HttpStatus status = HttpStatus.OK;
		
		this.service.updateAccount(accountViewModel, bindingResult);

		return new ResponseEntity<>(message, status);
	}
	
	//TODO add DTO to confirm that user deleting account is an actual user
	@DeleteMapping()
	public ResponseEntity<String> deleteAccount(@RequestParam String accountID) {
		String message = "Account deleted succesfully !";
		HttpStatus status =  HttpStatus.OK;
		
		try {
			this.service.deleteAccount(accountID);
			
		} catch(Exception e) {
			this.log.error(e.getMessage());
			message = e.getMessage(); 
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<>(message, status);
	}
	

}
