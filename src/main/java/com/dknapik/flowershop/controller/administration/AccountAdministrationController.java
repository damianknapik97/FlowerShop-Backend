package com.dknapik.flowershop.controller.administration;

import com.dknapik.flowershop.constants.administration.AccountAdministrationMessage;
import com.dknapik.flowershop.constants.sorting.AccountSortingProperty;
import com.dknapik.flowershop.dto.MessageResponseDTO;
import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.account.AccountAdministrativeDetailsDTO;
import com.dknapik.flowershop.dto.account.AccountEmployeeDetailsDTO;
import com.dknapik.flowershop.mapper.AccountMapper;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.services.administration.AccountAdministrationService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@CrossOrigin
@RequestMapping("/account-administration")
@RestController
@ToString
@Log4j2
public class AccountAdministrationController {
    private final AccountAdministrationService service;
    private final AccountMapper mapper;

    public AccountAdministrationController(AccountAdministrationService service, AccountMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Searches account that is related to provided order ID parameter. After that, Employee level account information
     * are extracted and composed into AccountEmployeeDetailsDTO and returned.
     * In case no Account was found, ResourceNotFound runtime exception is thrown.
     * This end point is accessible accounts with either admin or employee level privileges.
     * <p>
     * TODO: Add Test
     */
    @GetMapping("/order")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    ResponseEntity<AccountEmployeeDetailsDTO> retrieveEmployeeAccDetailsFromOrder(
            @Valid @RequestParam("id") UUID orderID) {
        log.traceEntry();

        Account retrievedAccount = service.retrieveAccountByOrderID(orderID);
        AccountEmployeeDetailsDTO responseDTO = mapper.mapToAccountEmployeeDetailsDTO(retrievedAccount);

        log.traceExit();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /**
     * Retrieves one page of accounts.
     * This end point is accessible only for accounts with admin level privileges.
     *
     * @param page             - Which page should be retrieved from the database.
     * @param numberOfElements - How many elements should page contain.
     * @param sortingProperty  - Which sorting method should be used
     * @return RestResponsePage containing Account entities.
     * <p>
     * TODO: Add Test
     */
    @GetMapping("/page")
    @Secured("ROLE_ADMIN")
    ResponseEntity<RestResponsePage<AccountAdministrativeDetailsDTO>> retrieveAccountsPage(
            @Valid @RequestParam(value = "page", defaultValue = "0") int page,
            @Valid @RequestParam(value = "elements", defaultValue = "20") int numberOfElements,
            @Valid @RequestParam(value = "sorting", defaultValue = "NONE") AccountSortingProperty sortingProperty) {
        log.traceEntry();

        RestResponsePage<Account> accountPage = service.retrieveAccountsPage(page, numberOfElements, sortingProperty);
        RestResponsePage<AccountAdministrativeDetailsDTO> mappedPage =
                mapper.mapToAccountAdministrativeDetailsDTOPage(accountPage);

        log.traceExit();
        return new ResponseEntity<>(mappedPage, HttpStatus.OK);
    }

    /**
     * Update details of account in provided DTO, by retrieving if from database based on ID, checking its existence,
     * and replacing all the possible fields with provided DTO values.
     */
    @PutMapping()
    @Secured("ROLE_ADMIN")
    ResponseEntity<MessageResponseDTO> updateAccountAdministrativeDetails(
            @Valid @RequestBody AccountAdministrativeDetailsDTO accountDetailsDTO) {
        log.traceEntry();

        service.updateAccount(accountDetailsDTO);

        log.traceExit();
        return new ResponseEntity<>(new MessageResponseDTO(AccountAdministrationMessage.ACCOUNT_UPDATED_SUCCESSFULLY),
                HttpStatus.OK);
    }

    /**
     * Retrieves and returns set containing all available sorting properties
     * that can be later used in account page retrieval
     */
    @GetMapping("/sorting")
    @Secured("ROLE_ADMIN")
    ResponseEntity<Set<AccountSortingProperty>> retrieveSortingProperties() {
        log.traceEntry();

        Set<AccountSortingProperty> sortingProperties = service.retrieveSortingProperties();

        log.traceExit();
        return new ResponseEntity<>(sortingProperties, HttpStatus.OK);
    }
}
