package com.dknapik.flowershop.controller;

import com.dknapik.flowershop.constants.AccountMessage;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.account.AccountDTO;
import com.dknapik.flowershop.dto.account.AccountDetailsDTO;
import com.dknapik.flowershop.dto.account.PasswordChangeDTO;
import com.dknapik.flowershop.exceptions.BindingException;
import com.dknapik.flowershop.exceptions.DataProcessingException;
import com.dknapik.flowershop.model.AccountRole;
import com.dknapik.flowershop.services.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * This class handles all requests related to account modification and
 * delegates them to appropriate functions in services and returns messages about data processing status
 * <p>
 * // TODO: Refactor this class :
 * - Exceptions should be replaced by Runtime exceptions
 * - Entity/DTO mapping should be performed here instead of service
 * - No need for custom handling of BindingResults
 *
 * @author Damian
 */
@RestController
@RequestMapping("/account")
@CrossOrigin
@Log4j2
final class AccountController {
    private final AccountService service; // Processes account data

    @Autowired
    AccountController(AccountService service) {
        this.service = service;
    }

    /**
     * Create new user account if all provided details are valid and provided user doesn't exists in db.
     * Additionally ensure that provided role is set to USER.
     *
     * @param accountDto    - dto containg basic informations needed to register new user.
     * @param bindingResult - Manual control of results of provided data binding to dto.
     * @return string message with informations about data processing status.
     */
    @PostMapping()
    ResponseEntity<MessageResponseDTO> createAccountWithUserRole(@Valid @RequestBody AccountDTO accountDto,
                                                     BindingResult bindingResult) {
        log.traceEntry();
        MessageResponseDTO response = new MessageResponseDTO(AccountMessage.ENTITY_CREATION_SUCCESSFUL.toString());
        HttpStatus status = HttpStatus.OK;

        // TODO: For presentation purposes changed to administrator
        accountDto.setRole(AccountRole.ROLE_ADMIN);

        try {
            this.service.createNewUser(accountDto, bindingResult);    // Process provided data
        } catch (BindingException | DataProcessingException e) {
            log.warn("Exception creating new account", e);
            response = new MessageResponseDTO(e.getMessage());
            status = e.getHttpStatus();
        }

        log.traceExit();
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
    public ResponseEntity<AccountDetailsDTO> retrieveAccount(Principal principal) {
        log.traceEntry();

        AccountDetailsDTO acc = null;
        HttpStatus status = HttpStatus.OK;

        try {
            acc = this.service.retrieveAccountDetails(principal);
        } catch (DataProcessingException e) {
            log.warn("Exception retrieving account data", e);
            status = e.getHttpStatus();
        }

        log.traceExit();
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
    ResponseEntity<MessageResponseDTO> updateAccount(
            @Valid @RequestBody AccountDetailsDTO accountDetailsViewModel,
            BindingResult bindingResult,
            Principal principal) {
        log.traceEntry();

        MessageResponseDTO response = new MessageResponseDTO(AccountMessage.ENTITY_UPDATE_SUCCESSFUL.toString());
        HttpStatus status = HttpStatus.OK;

        try {
            this.service.updateAccount(accountDetailsViewModel, bindingResult, principal);
        } catch (BindingException | DataProcessingException e) {
            log.warn("Exception updating account data", e);
            response.setMessage(e.getMessage());
            status = e.getHttpStatus();
        }

        log.traceExit();
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
    ResponseEntity<MessageResponseDTO> updatePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDto,
                                                      BindingResult bindingResult,
                                                      Principal principal) {
        log.traceEntry();

        MessageResponseDTO response = new MessageResponseDTO(AccountMessage.ENTITY_UPDATE_SUCCESSFUL.toString());
        HttpStatus status = HttpStatus.OK;

        try {
            this.service.updatePassword(passwordChangeDto, bindingResult, principal);
        } catch (BindingException | DataProcessingException e) {
            log.warn("Exception changing password", e);
            response.setMessage(e.getMessage());
            status = e.getHttpStatus();
        }


        log.traceExit();
        return new ResponseEntity<>(response, status);
    }

    /**
     * User must be authenticated to perform this request.
     * Deletes account performing this request from database.
     *
     * @param password  - user input
     * @param principal - handle to spring security context to retrieve user performing request.
     * @return string message with information about data processing status.
     */
    @DeleteMapping()
    ResponseEntity<MessageResponseDTO> deleteAccount(@Valid @RequestParam("password") String password,
                                                     Principal principal) {
        log.traceEntry();

        MessageResponseDTO response = new MessageResponseDTO(AccountMessage.ENTITY_DELETE_SUCCESSFUL.toString());
        HttpStatus status = HttpStatus.OK;

        try {
            this.service.deleteAccount(password, principal);
        } catch (DataProcessingException e) {
            this.log.warn("Exception deleting account", e);
            response.setMessage(e.getMessage());
            status = e.getHttpStatus();
        }

        log.traceExit();
        return new ResponseEntity<>(response, status);
    }
}
