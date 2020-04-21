package com.dknapik.flowershop.services.administration;

import com.dknapik.flowershop.constants.administration.AccountAdministrationMessage;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.services.AccountService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@ToString
@Log4j2
@Service
public class AccountAdministrationService {
    private final AccountService accountService;
    private final AccountRepository repository;

    public AccountAdministrationService(AccountService accountService, AccountRepository repository) {
        this.accountService = accountService;
        this.repository = repository;
    }

    /**
     * Searches database for account with provided ID, and returns it.
     * If account was not found, ResourceNotFound runtime exception is thrown.
     */
    public Account retrieveAccount(UUID accountID) {
        log.traceEntry();
        Optional<Account> accountOptional = repository.findById(accountID);

        if (!accountOptional.isPresent()) {
            log.throwing(new ResourceNotFoundException(AccountAdministrationMessage.ACCOUNT_NOT_FOUND));
            throw new ResourceNotFoundException(AccountAdministrationMessage.ACCOUNT_NOT_FOUND);
        }

        log.traceExit();
        return accountOptional.get();
    }
}
