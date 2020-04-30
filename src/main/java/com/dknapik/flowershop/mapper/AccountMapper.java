package com.dknapik.flowershop.mapper;

import com.dknapik.flowershop.dto.RestResponsePage;
import com.dknapik.flowershop.dto.account.AccountAdministrativeDetailsDTO;
import com.dknapik.flowershop.dto.account.AccountDTO;
import com.dknapik.flowershop.dto.account.AccountEmployeeDetailsDTO;
import com.dknapik.flowershop.model.Account;
import com.dknapik.flowershop.utils.PasswordUtils;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@ToString
@Log4j2
public class AccountMapper implements Mapper<Account, AccountDTO> {
    private final ModelMapper modelMapper;
    private final PasswordUtils passwordUtils;

    public AccountMapper(ModelMapper modelMapper, PasswordUtils passwordUtils) {
        this.modelMapper = modelMapper;
        this.passwordUtils = passwordUtils;
    }

    /*
     * TODO: Improve this method to support Account nested classes
     */
    @Override
    public AccountDTO mapToDTO(Account model) {
        log.traceEntry();
        log.traceExit();
        return modelMapper.map(model, AccountDTO.class);
    }

    /*
     * TODO: Improve this method to support Account nested classes
     */
    @Override
    public Account mapToEntity(AccountDTO dto) {
        log.traceEntry();
        log.traceExit();
        return modelMapper.map(dto, Account.class);
    }

    /**
     * Extracts essential information for AccountEmployeeDetailsDTO from provided account entity. Creates
     * new mentioned DTO instance with mentioned details, and returns it.
     */
    public AccountEmployeeDetailsDTO mapToAccountEmployeeDetailsDTO(Account account) {
        log.traceEntry();

        AccountEmployeeDetailsDTO accountEmployeeDetailsDTO = new AccountEmployeeDetailsDTO(
                account.getName(),
                account.getEmail()
        );

        log.traceExit();
        return accountEmployeeDetailsDTO;
    }

    /**
     * Extracts variable data from Account entity, creates new AccountAdministrativeDetailsDTO and assigns mentioned
     * data to available fields.
     */
    public AccountAdministrativeDetailsDTO mapToAccountAdministrativeDetailsDTO(Account account) {
        log.traceEntry();
        log.traceExit();
        return modelMapper.map(account, AccountAdministrativeDetailsDTO.class);
    }

    /**
     * Map RestResponsePage content from Account to AccountAdministrativeDetailsDTO and return new RestResponsePage
     * object with mentioned content.
     */
    public RestResponsePage<AccountAdministrativeDetailsDTO> mapToAccountAdministrativeDetailsDTOPage(
            RestResponsePage<Account> accountsPage) {
        log.traceEntry();

        List<AccountAdministrativeDetailsDTO> mappedContent = new LinkedList<>();
        for (Account account : accountsPage.getContent()) {
            mappedContent.add(mapToAccountAdministrativeDetailsDTO(account));
        }

        log.traceExit();
        return new RestResponsePage<>(mappedContent, accountsPage);
    }

    /**
     * Maps provided source object to destination object.
     * No new object is created, only mapped destination object is returned
     *
     * Password is handled separately by PasswordUtilities. By handled separately, I mean it is checked in the
     * context of source password matching the destination password. If previous statement is false, source password
     * is encrypted and it replaces destination password.
     */
    public Account mapToExistingEntity(AccountAdministrativeDetailsDTO source, Account destination) {
        log.traceEntry();

        String newPassword = source.getPassword();
        String oldPassword = destination.getPassword();

        modelMapper.map(source, destination);
        destination.setPassword(passwordUtils.replacePassword(oldPassword, newPassword));

        log.traceExit();
        return destination;
    }

}
