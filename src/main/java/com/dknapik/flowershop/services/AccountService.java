package com.dknapik.flowershop.services;

import com.dknapik.flowershop.constants.AccountMessage;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.dto.account.AccountDTO;
import com.dknapik.flowershop.dto.account.AccountDetailsDTO;
import com.dknapik.flowershop.dto.account.PasswordChangeDTO;
import com.dknapik.flowershop.exceptions.BindingException;
import com.dknapik.flowershop.exceptions.DataProcessingException;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.Account;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

/**
 * This class deals with all the operations done on user account currently it is:
 * -Creating account
 * -Updating account informations
 * -Retrieving current account informations
 * -Deleting account
 *
 * @author Damian
 */
@Service
@ToString
@Log4j2
public final class AccountService {
    private final ModelMapper mapper;             // less messy dto - model mapping
    private final AccountRepository accountRepo; // database access
    private final ApplicationContext context;    // retrieve existing beans

    @Autowired
    public AccountService(AccountRepository accountRepo, ApplicationContext context, ModelMapper modelMapper) {
        this.accountRepo = accountRepo;
        this.context = context;
        this.mapper = modelMapper;
    }

    /**
     * Create new user and save its credentials to database.
     *
     * @param accountDto    - dto containg basic user credentials.
     * @param bindingResult - request to dto mapping results.
     * @return - dto containing message about data processing results.
     * @throws BindingException        - mapping request to dto failed.
     * @throws DataProcessingException - account already exists.
     */
    public void createNewUser(@Valid AccountDTO accountDto,
                              BindingResult bindingResult)
            throws BindingException, DataProcessingException {
        if (bindingResult.hasErrors()) {
            log.error(AccountMessage.NEW_ENTITY_CREATION_ERROR.toString());
            throw new BindingException(AccountMessage.NEW_ENTITY_CREATION_ERROR, accountDto.getClass());
        }

        if (this.accountRepo.existsByName(accountDto.getName())) {
            log.warn(AccountMessage.ALREADY_EXISTS.toString());
            throw new DataProcessingException(AccountMessage.ALREADY_EXISTS);
        }

        accountDto.setPassword(
                context.getBean(PasswordEncoder.class).encode(accountDto.getPassword()));  // Encode password
        this.accountRepo.saveAndFlush(mapper.map(accountDto, Account.class));
    }

    /**
     * Retrieve single account entity, check if exists and return
     *
     * @param accountName - account login
     * @return single Account entity
     */
    public Account retrieveAccountByName(String accountName) {
        Optional<Account> account = accountRepo.findByName(accountName);
        return account.orElseThrow(() ->
                new ResourceNotFoundException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR));
    }

    /**
     * Retrieves user details from database by retrieving currently logged user details from spring security context.
     *
     * @param principal - handle used to access spring security context.
     * @return additional details about account sending request.
     * @throws DataProcessingException - couldn't find currently logged user details.
     */
    public AccountDetailsDTO retrieveAccountDetails(Principal principal)
            throws DataProcessingException {
        Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
                () -> new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR));

        return this.mapper.map(acc, AccountDetailsDTO.class);
    }

    /**
     * Update account side details - ones that doesn't change how provided account is accessed.
     *
     * @param accDetailsDto
     * @param bindingResult - request to dto mapping results.`
     * @param principal     - handle used to access spring security context to retrieve account name sending request.
     * @throws BindingException        - mapping request to dto failed.
     * @throws DataProcessingException - couldn't retrieve currently logged user from security context.
     */
    public void updateAccount(@Valid AccountDetailsDTO accDetailsDto,
                              BindingResult bindingResult,
                              Principal principal)
            throws BindingException, DataProcessingException {
        if (bindingResult.hasErrors()) {
            log.error(AccountMessage.ENTITY_UPDATE_ERROR.toString());
            throw new BindingException(AccountMessage.ENTITY_UPDATE_ERROR,
                    accDetailsDto.getClass());
        }

        Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
                () -> new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR));

        this.mapper.map(accDetailsDto, acc);
        this.accountRepo.saveAndFlush(acc);
    }

    /**
     * Checks if provided account exists in database, and updated it if true
     *
     * @param account - Account Entity that will be saved in database if exists
     */
    public void updateAccount(Account account) {
        /* Check if account exists, if not, we are not performing update but save so throw an exception */
        if (!accountRepo.existsById(account.getId())) {
            throw new ResourceNotFoundException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR);
        } else {
            accountRepo.saveAndFlush(account);
        }
    }

    /**
     * Checks if provided actual password matches the one in database and replaces it with new password.
     *
     * @param passwordChangeDto - dto containing actual password, new password and confirmation password.
     * @param bindingResult     - request to dto mapping results.
     * @param principal         - handle used to access spring security context to retrieve account name sending request.
     * @throws BindingException        - mapping request to dto failed.
     * @throws DataProcessingException - thrown when provided passwords doesn't match.
     */
    public void updatePassword(@Valid PasswordChangeDTO passwordChangeDto,
                               BindingResult bindingResult,
                               Principal principal)
            throws BindingException, DataProcessingException {
        if (bindingResult.hasErrors()) {
            log.error(AccountMessage.ENTITY_UPDATE_ERROR.toString());
            throw new BindingException(AccountMessage.ENTITY_UPDATE_ERROR,
                    passwordChangeDto.getClass());
        }

        Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
                () -> new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR)
        );

        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        if (!passwordChangeDto.getNewPassword().contentEquals(passwordChangeDto.getNewPasswordConfirmation())) {
            log.warn(AccountMessage.PASSWORD_UPDATE_ERROR.toString());
            throw new DataProcessingException(AccountMessage.PASSWORD_UPDATE_ERROR);
        }

        acc.setPassword(encoder.encode(passwordChangeDto.getNewPassword()));
        this.accountRepo.saveAndFlush(acc);
    }

    /**
     * If provided password matches current users one, then delete current account from database.
     *
     * @param password  - user input.
     * @param principal - handle used to access spring security context to retrieve account name sending request.
     * @throws DataProcessingException - Either provided password doesn't match or currently logged user couldn't be found.
     */
    public void deleteAccount(String password,
                              Principal principal)
            throws DataProcessingException {
        if (password != null) {
            Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
                    () -> new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR)
            );

            PasswordEncoder encoder = context.getBean(PasswordEncoder.class);

            if (encoder.matches(password, acc.getPassword())) {
                this.accountRepo.delete(acc);
            } else {
                throw new DataProcessingException(AccountMessage.ENTITY_DELETE_ERROR);
            }
        }

    }
}
