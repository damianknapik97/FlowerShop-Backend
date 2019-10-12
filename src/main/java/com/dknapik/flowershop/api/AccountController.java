package com.dknapik.flowershop.api;


import java.security.Principal;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.fasterxml.jackson.databind.node.TextNode;
import com.dknapik.flowershop.dto.MessageResponseDto;
import com.dknapik.flowershop.dto.account.AccountDetailsDto;
import com.dknapik.flowershop.dto.account.AccountDto;
import com.dknapik.flowershop.dto.account.PasswordChangeDto;
import com.dknapik.flowershop.dto.account.PasswordDto;
import com.dknapik.flowershop.exceptions.BindingException;
import com.dknapik.flowershop.exceptions.DataProcessingException;
import com.dknapik.flowershop.exceptions.MappingException;
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

	@PostMapping()
	public ResponseEntity<MessageResponseDto> createAccount(@Valid @RequestBody AccountDto accountViewModel,
												BindingResult bindingResult) {
		MessageResponseDto response;
		HttpStatus status = HttpStatus.OK;
		
		try {
			response = this.service.createNewUser(accountViewModel, bindingResult);
		} catch (BindingException | DataProcessingException e) {
			log.warn("Exception creating new account", e);
			response = new MessageResponseDto(e.getMessage());
			status = e.getHttpStatus();
		}
		
		return new ResponseEntity<>(response, status);
	}
	
	@GetMapping()
	public ResponseEntity<AccountDetailsDto> retrieveAccount(Principal principal) {
		AccountDetailsDto acc = null;
		HttpStatus status = HttpStatus.OK;
		
			try {
				acc = this.service.retrieveAccountDetails(principal);
			} catch (DataProcessingException e) {
				log.warn("Exception retrieving account data", e);
				status = e.getHttpStatus();
			}
		
		return new ResponseEntity<>(acc, status);
	}
	
	@PutMapping()
	public ResponseEntity<MessageResponseDto> updateAccount(@Valid @RequestBody AccountDetailsDto accountDetailsViewModel,
												BindingResult bindingResult,
												Principal principal) {
		MessageResponseDto response = new MessageResponseDto();
		response.setMessage("Account details updated succesfully !");
		HttpStatus status = HttpStatus.OK;
		
		try {
			this.service.updateAccount(accountDetailsViewModel, bindingResult, principal);
		} catch (BindingException | DataProcessingException e) {
			log.warn("Exception updating account data", e);
			response.setMessage(e.getMessage());
			status = e.getHttpStatus();
		}

		return new ResponseEntity<>(response, status);
	}
	
	@PutMapping("/password")
	public ResponseEntity<MessageResponseDto> updatePassword(@Valid @RequestBody PasswordChangeDto passwordChangeViewModel,
												 BindingResult bindingResult,
												 Principal principal) {
		
		MessageResponseDto response = new MessageResponseDto();
		response.setMessage("Password updated succesfully !");
		HttpStatus status = HttpStatus.OK;
		
		try {
			this.service.updatePassword(passwordChangeViewModel, bindingResult, principal);
		} catch (BindingException | DataProcessingException e) {
			this.log.warn("Exception changing password", e);
			response.setMessage(e.getMessage());
			status = e.getHttpStatus();
		}
		
		return new ResponseEntity<>(response, status);
	}
	//@RequestParam("password")
	@DeleteMapping()
	public ResponseEntity<MessageResponseDto> deleteAccount(@RequestBody String password,
												Principal principal) {
		MessageResponseDto response = new MessageResponseDto();
		response.setMessage("Account deleted succesfully !");
		HttpStatus status =  HttpStatus.OK;
			try {
				this.service.deleteAccount(password, principal);
			} catch (DataProcessingException | MappingException e) {
				this.log.warn("Exception deleting account", e);
				response.setMessage(e.getMessage());
				status = e.getHttpStatus();
			}

		return new ResponseEntity<>(response, status);
	}
	

}
