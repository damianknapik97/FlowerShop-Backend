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
import org.apache.logging.log4j.Level;
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
 * TODO : Refactor this class: Replace Exceptions with RuntimeExceptions,
 *        Return only entities - convert to DTO in controller
 *
 *
 * @author Damian
 */
@Service
@ToString
@Log4j2
public final class AccountService {
    private final ModelMapper mapper;            // less messy dto - model mapping
    private final AccountRepository accountRepo; // database access
    private final ApplicationContext context;    // retrieve existing beans
    private final PasswordEncoder encoder;       // Encode Account passwords in database

    @Autowired
    public AccountService(ModelMapper mapper,
                          AccountRepository accountRepo,
                          ApplicationContext context,
                          PasswordEncoder encoder) {
        this.mapper = mapper;
        this.accountRepo = accountRepo;
        this.context = context;
        this.encoder = encoder;
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
        log.traceEntry();

        if (bindingResult.hasErrors()) {
            log.throwing(Level.ERROR,
                    new BindingException(AccountMessage.NEW_ENTITY_CREATION_ERROR, accountDto.getClass()));
            throw new BindingException(AccountMessage.NEW_ENTITY_CREATION_ERROR, accountDto.getClass());
        }

        if (this.accountRepo.existsByName(accountDto.getName())) {
            log.throwing(Level.WARN, new DataProcessingException(AccountMessage.ALREADY_EXISTS));
            throw new DataProcessingException(AccountMessage.ALREADY_EXISTS);
        }

        accountDto.setPassword(encoder.encode(accountDto.getPassword()));  // Encode password
        this.accountRepo.saveAndFlush(mapper.map(accountDto, Account.class));

        log.traceExit();
    }

    /**
     * Retrieve single account entity, check if exists and return
     *
     * @param accountName - account login
     * @return single Account entity
     */
    public Account retrieveAccountByName(String accountName) {
        log.traceEntry();

        Optional<Account> account = accountRepo.findByName(accountName);
        if (!account.isPresent()) {
            log.throwing(Level.ERROR, new ResourceNotFoundException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR));
            throw new ResourceNotFoundException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR);
        }

        log.traceExit();
        return account.get();
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
        log.traceEntry();

        Optional<Account> acc = this.accountRepo.findByName(principal.getName());
        if (!acc.isPresent()) {
            log.throwing(Level.ERROR, new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR));
            throw new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR);
        }

        log.traceExit();
        return this.mapper.map(acc.get(), AccountDetailsDTO.class);
    }

    /**
     * Update account side details - ones that doesn't change how provided account is accessed.
     *
     * @param accDetailsDto - DTO object containing details that will be updated in account.
     * @param bindingResult - request to dto mapping results.`
     * @param principal     - handle used to access spring security context to retrieve account name sending request.
     * @throws BindingException        - mapping request to dto failed.
     * @throws DataProcessingException - couldn't retrieve currently logged user from security context.
     */
    public void updateAccount(@Valid AccountDetailsDTO accDetailsDto,
                              BindingResult bindingResult,
                              Principal principal)
            throws BindingException, DataProcessingException {
        log.traceEntry();

        if (bindingResult.hasErrors()) {
            log.throwing(Level.ERROR,
                    new BindingException(AccountMessage.ENTITY_UPDATE_ERROR, accDetailsDto.getClass()));
            throw new BindingException(AccountMessage.ENTITY_UPDATE_ERROR, accDetailsDto.getClass());
        }

        Optional<Account> acc = this.accountRepo.findByName(principal.getName());
        if (!acc.isPresent()) {
            log.throwing(Level.ERROR, new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR));
            throw new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR);
        }

        this.mapper.map(accDetailsDto, acc.get());
        this.accountRepo.saveAndFlush(acc.get());

        log.traceExit();
    }

    /**
     * Checks if provided account exists in database, and updated it if true
     *
     * @param account - Account Entity that will be saved in database if exists
     */
    public void updateAccount(Account account) {
        log.traceEntry();

        /* Check if account exists, if not, we are not performing update but save so throw an exception */
        if (!accountRepo.existsById(account.getId())) {
            log.throwing(Level.ERROR, new ResourceNotFoundException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR));
            throw new ResourceNotFoundException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR);
        } else {
            accountRepo.saveAndFlush(account);
        }

        log.traceExit();
    }

    /**
     * Checks if provided actual password matches the one in database and replaces it with new password.
     *
     * @param passwordChangeDto - dto containing actual password, new password and confirmation password.
     * @param bindingResult     - request to dto mapping results.
     * @param principal        - handle used to access spring security context to retrieve account name sending request.
     * @throws BindingException        - mapping request to dto failed.
     * @throws DataProcessingException - thrown when provided passwords doesn't match.
     */
    public void updatePassword(@Valid PasswordChangeDTO passwordChangeDto,
                               BindingResult bindingResult,
                               Principal principal)
            throws BindingException, DataProcessingException {
        log.traceEntry();

        if (bindingResult.hasErrors()) {
            log.error(AccountMessage.ENTITY_UPDATE_ERROR.toString());
            throw new BindingException(AccountMessage.ENTITY_UPDATE_ERROR,
                    passwordChangeDto.getClass());
        }

        Optional<Account> acc = this.accountRepo.findByName(principal.getName());
        if (!acc.isPresent()) {
            log.throwing(Level.ERROR, new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR));
            throw new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR);
        }

        if (!passwordChangeDto.getNewPassword().contentEquals(passwordChangeDto.getNewPasswordConfirmation())) {
            log.throwing(Level.WARN, new DataProcessingException(AccountMessage.PASSWORD_UPDATE_ERROR));
            throw new DataProcessingException(AccountMessage.PASSWORD_UPDATE_ERROR);
        }

        acc.get().setPassword(encoder.encode(passwordChangeDto.getNewPassword()));
        this.accountRepo.saveAndFlush(acc.get());

        log.traceExit();
    }

    /**
     * If provided password matches current users one, then delete current account from database.
     *
     * @param password  - user input.
     * @param principal - handle used to access spring security context to retrieve account name sending request.
     * @throws DataProcessingException - Either provided password doesn't match
     * or currently logged user couldn't be found.
     */
    public void deleteAccount(String password,
                              Principal principal)
            throws DataProcessingException {
        log.traceEntry();

        if (password != null) {
            Optional<Account> acc = this.accountRepo.findByName(principal.getName());
            if (!acc.isPresent()) {
                log.throwing(Level.ERROR, new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR));
                throw new DataProcessingException(AccountMessage.ENTITY_DETAILS_RETRIEVAL_ERROR);
            }

            if (encoder.matches(password, acc.get().getPassword())) {
                this.accountRepo.delete(acc.get());
            } else {
                log.throwing(Level.ERROR, new DataProcessingException(AccountMessage.ENTITY_DELETE_ERROR));
                throw new DataProcessingException(AccountMessage.ENTITY_DELETE_ERROR);
            }
        }

        log.traceExit();
    }
}
