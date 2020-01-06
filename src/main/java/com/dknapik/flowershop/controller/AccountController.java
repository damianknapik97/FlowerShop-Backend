package com.dknapik.flowershop.controller;

import java.security.Principal;

import javax.validation.Valid;

import com.dknapik.flowershop.constants.AccountMessage;
import lombok.extern.log4j.Log4j2;
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
import com.dknapik.flowershop.dto.MessageResponseDto;
import com.dknapik.flowershop.dto.account.AccountDetailsDto;
import com.dknapik.flowershop.dto.account.AccountDto;
import com.dknapik.flowershop.dto.account.PasswordChangeDto;
import com.dknapik.flowershop.exceptions.BindingException;
import com.dknapik.flowershop.exceptions.DataProcessingException;
import com.dknapik.flowershop.services.AccountService;

/**
 * This class handles all requests related to account modification and
 * delegates them to appropriate functions in services and returns messages about data processing status
 *
 * @author Damian
 */
@RestController
@RequestMapping("/account")
@CrossOrigin
@Log4j2
public class AccountController {
    private final AccountService service; // Processes account data

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    /**
     * Create new user account if all provided details are valid and provided user doesn't exists in db.
     *
     * @param accountDto    - dto containg basic informations needed to register new user.
     * @param bindingResult - Manual control of results of provided data binding to dto.
     * @return string message with informations about data processing status.
     */
    @PostMapping()
    public ResponseEntity<MessageResponseDto> createAccount(@Valid @RequestBody AccountDto accountDto,
                                                            BindingResult bindingResult) {
        MessageResponseDto response = new MessageResponseDto(AccountMessage.ENTITY_CREATION_SUCCESSFUL.toString());
        HttpStatus status = HttpStatus.OK;

        try {
            this.service.createNewUser(accountDto, bindingResult);    // Process provided data
        } catch (BindingException | DataProcessingException e) {
            log.warn("Exception creating new account", e);
            response = new MessageResponseDto(e.getMessage());
            status = e.getHttpStatus();
        }

        return new ResponseEntity<>(response, status);
    }

    /**
     * User must be authenticated to perform this request.
     * Retrieve all current account details,
     * that are non essential to accessing current account.
     *
     * @param principal - handle to spring security context to retrieve user performing request.
     * @return string message with informations about data processing status.
     */
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

    /**
     * User must be authenticated to perform this request.
     * Update all non account access essential account details from user input.
     *
     * @param principal     - handle to spring security context to retrieve user performing request.
     * @param bindingResult - results of binding request to dto.
     * @return - string message with informations about data processing status.
     */
    @PutMapping()
    public ResponseEntity<MessageResponseDto> updateAccount(
            @Valid @RequestBody AccountDetailsDto accountDetailsViewModel,
            BindingResult bindingResult,
            Principal principal) {
        MessageResponseDto response = new MessageResponseDto(AccountMessage.ENTITY_UPDATE_SUCCESSFUL.toString());
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

    /**
     * User must be authenticated to perform this request.
     * Update password that is used to login to current account
     *
     * @param passwordChangeDto - contains user provided current password, new password and confirmation password
     * @param bindingResult     - Manual control of results of provided data binding to dto.
     * @param principal         - handle to spring security context to retrieve user performing request.
     * @return string message with informations about data processing status.
     */
    @PutMapping("/password")
    public ResponseEntity<MessageResponseDto> updatePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto,
                                                             BindingResult bindingResult,
                                                             Principal principal) {
        MessageResponseDto response = new MessageResponseDto(AccountMessage.ENTITY_UPDATE_SUCCESSFUL.toString());
        HttpStatus status = HttpStatus.OK;

        try {
            this.service.updatePassword(passwordChangeDto, bindingResult, principal);
        } catch (BindingException | DataProcessingException e) {
            this.log.warn("Exception changing password", e);
            response.setMessage(e.getMessage());
            status = e.getHttpStatus();
        }

        return new ResponseEntity<>(response, status);
    }

    /**
     * User must be authenticated to perform this request.
     * Deletes account performing this request from database.
     *
     * @param password  - user input
     * @param principal - handle to spring security context to retrieve user performing request.
     * @return string message with informations about data processing status.
     */
    @DeleteMapping()
    public ResponseEntity<MessageResponseDto> deleteAccount(@RequestParam("password") String password,
                                                            Principal principal) {
        MessageResponseDto response = new MessageResponseDto(AccountMessage.ENTITY_DELETE_SUCCESSFUL.toString());
        HttpStatus status = HttpStatus.OK;

        try {
            this.service.deleteAccount(password, principal);
        } catch (DataProcessingException e) {
            this.log.warn("Exception deleting account", e);
            response.setMessage(e.getMessage());
            status = e.getHttpStatus();
        }

        return new ResponseEntity<>(response, status);
    }
}
