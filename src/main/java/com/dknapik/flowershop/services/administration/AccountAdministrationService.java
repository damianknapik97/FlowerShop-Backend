package com.dknapik.flowershop.services.administration;

import com.dknapik.flowershop.constants.administration.AccountAdministrationMessage;
import com.dknapik.flowershop.constants.sorting.AccountSortingProperty;
import com.dknapik.flowershop.database.AccountRepository;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.account.AccountAdministrativeDetailsDTO;
import com.dknapik.flowershop.exceptions.runtime.ResourceNotFoundException;
import com.dknapik.flowershop.mapper.AccountMapper;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.services.AccountService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ToString
@Log4j2
@Service
public final class AccountAdministrationService {
    private final AccountService accountService;
    private final AccountRepository repository;
    private final AccountMapper accountMapper;

    public AccountAdministrationService(AccountService accountService,
                                        AccountRepository repository,
                                        AccountMapper accountMapper) {
        this.accountService = accountService;
        this.repository = repository;
        this.accountMapper = accountMapper;
    }

    public Set<AccountSortingProperty> retrieveSortingProperties() {
        log.traceEntry();
        log.traceExit();
        return EnumSet.allOf(AccountSortingProperty.class);
    }

    /**
     * Searches database for account with provided ID, and returns it.
     * If account was not found, ResourceNotFound runtime exception is thrown.
     * <p>
     * TODO: Add Test
     */
    public Account retrieveAccountByOrderID(UUID orderID) {
        log.traceEntry();
        Optional<Account> accountOptional = repository.findByOrderID(orderID);

        if (!accountOptional.isPresent()) {
            log.throwing(new ResourceNotFoundException(AccountAdministrationMessage.ACCOUNT_NOT_FOUND));
            throw new ResourceNotFoundException(AccountAdministrationMessage.ACCOUNT_NOT_FOUND);
        }

        log.traceExit();
        return accountOptional.get();
    }

    /**
     * Checks if provided account exists by ID. If true, account is updated by provided details. If not,
     * ResourceNotFound runtime exception is thrown.
     *
     * @param accountDetails - Account details to update.
     */
    public void updateAccount(AccountAdministrativeDetailsDTO accountDetails) {
        log.traceEntry();

        Optional<Account> retrievedAccount = repository.findByOrderID(accountDetails.getId());
        if (!retrievedAccount.isPresent()) {
            log.throwing(new ResourceNotFoundException(AccountAdministrationMessage.ACCOUNT_NOT_FOUND));
            throw new ResourceNotFoundException(AccountAdministrationMessage.ACCOUNT_NOT_FOUND);
        }

        log.debug(() -> "Account before update: " + retrievedAccount.get().toString());
        Account accountToUpdate = accountMapper.mapToExistingEntity(accountDetails, retrievedAccount.get());
        log.debug(() -> "Account after update: " + accountToUpdate.toString());
        this.repository.saveAndFlush(accountToUpdate);

        log.traceExit();
    }

    /**
     * Creates ResponsePage containing sorted account entities.
     * Page number and its size together with sorting is determined based on provided properties.
     *
     * @param pageNumber      - which page should be retrieved
     * @param pageSize        - how large should the page be.
     * @param sortingProperty - how accounts should be ordered when retrieved from database
     * @return RestResponsePage with Account entities.
     */
    public RestResponsePage<Account> retrieveAccountsPage(int pageNumber,
                                                          int pageSize,
                                                          AccountSortingProperty sortingProperty) {
        log.traceEntry();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Account> accountPage = retrieveSortedPage(pageable, sortingProperty);


        log.traceExit();
        return new RestResponsePage<>(accountPage);
    }

    /**
     * Determines which sorting property was desired by the user basing on provided sorting property.
     * Then, according repository function is invoked and its results are returned.
     *
     * @param pageable        - Page request used in repository for page retrieval
     * @param sortingProperty - Enum containing name of desired sorting method
     * @return Page with accounts
     */
    private Page<Account> retrieveSortedPage(Pageable pageable, AccountSortingProperty sortingProperty) {
        log.traceEntry();

        Page<Account> accountsPage;
        switch (sortingProperty) {
            case CREATION_DATE_DESC:
                accountsPage = repository.findAllByOrderByCreationDateDesc(pageable);
                break;
            case CREATION_DATE_ASC:
                accountsPage = repository.findAllByOrderByCreationDateAsc(pageable);
                break;
            case ROLE_DESC:
                accountsPage = repository.findAllByOrderByRoleDesc(pageable);
                break;
            case ROLE_ASC:
                accountsPage = repository.findAllByOrderByRoleAsc(pageable);
                break;
            default:
                accountsPage = repository.findAll(pageable);
        }

        log.traceExit();
        return accountsPage;
    }
}
