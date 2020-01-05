package com.dknapik.flowershop.services;

import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.dto.account.AccountDetailsDto;
import com.dknapik.flowershop.dto.account.AccountDto;
import com.dknapik.flowershop.dto.account.PasswordChangeDto;
import com.dknapik.flowershop.exceptions.BindingException;
import com.dknapik.flowershop.exceptions.DataProcessingException;
import com.dknapik.flowershop.model.Account;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.security.Principal;

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
public class AccountService {
    private final Logger log = LogManager.getLogger(getClass().getName());
    private final ModelMapper mapper;             // less messy dto - model mapping
    private final AccountRepository accountRepo; // database access
    private final ApplicationContext context;    // retrieve existing beans

    @Autowired
    public AccountService(AccountRepository accountRepo, ApplicationContext context) {
        this.accountRepo = accountRepo;
        this.context = context;
        this.mapper = new ModelMapper();
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
    public void createNewUser(AccountDto accountDto,
                              BindingResult bindingResult)
            throws BindingException, DataProcessingException {
        if (bindingResult.hasErrors()) {
            log.error("Couldn't create new account because provided data isn't valid and properly binded");
            throw new BindingException(bindingResult.getFieldError().getDefaultMessage(), accountDto.getClass());
        }

        if (this.accountRepo.existsByName(accountDto.getName())) {
            log.warn("Account already  exists");
            throw new DataProcessingException("Account with provided login already exists");
        }

        accountDto.setPassword(
                context.getBean(PasswordEncoder.class).encode(accountDto.getPassword()));  // Encode password
        this.accountRepo.saveAndFlush(mapper.map(accountDto, Account.class));
    }

    /**
     * Retrieves user details from database by retrieving currently logged user details from spring security context.
     *
     * @param principal - handle used to access spring security context.
     * @return additional details about account sending request.
     * @throws DataProcessingException - couldn't find currently logged user details.
     */
    public AccountDetailsDto retrieveAccountDetails(Principal principal)
            throws DataProcessingException {
        Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
                () -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
        );

        return this.mapper.map(acc, AccountDetailsDto.class);
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
    public void updateAccount(AccountDetailsDto accDetailsDto,
                              BindingResult bindingResult,
                              Principal principal)
            throws BindingException, DataProcessingException {
        if (bindingResult.hasErrors()) {
            log.error("Couldn't update account details because provided data couldn't be properly binded");
            throw new BindingException(bindingResult.getFieldError().getDefaultMessage(),
                    accDetailsDto.getClass());
        }

        Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
                () -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
        );

        this.mapper.map(accDetailsDto, acc);
        this.accountRepo.saveAndFlush(acc);
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
    public void updatePassword(PasswordChangeDto passwordChangeDto,
                               BindingResult bindingResult,
                               Principal principal)
            throws BindingException, DataProcessingException {
        if (bindingResult.hasErrors()) {
            log.error("Couldn't update account password because provided data wasn't properly binded");
            throw new BindingException(bindingResult.getFieldError().getDefaultMessage(),
                    passwordChangeDto.getClass());
        }

        Account acc = this.accountRepo.findByName(principal.getName()).orElseThrow(
                () -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
        );

        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        if (!passwordChangeDto.getNewPassword().contentEquals(passwordChangeDto.getNewPasswordConfirmation())) {
            log.warn("Couldn't update account password because provided new password doesn't match confirmation password");
            throw new DataProcessingException("Provided password doesn't match actual password");
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
                    () -> new DataProcessingException("Error, couldn't retrieve currently logged user details")
            );

            PasswordEncoder encoder = context.getBean(PasswordEncoder.class);

            if (encoder.matches(password, acc.getPassword())) {
                this.accountRepo.delete(acc);
            } else {
                throw new DataProcessingException("Provided password doesn't match");
            }
        }

    }
}
