package com.dknapik.flowershop.controller.administration;

import com.dknapik.flowershop.dto.account.AccountEmployeeDetailsDTO;
import com.dknapik.flowershop.mapper.AccountMapper;
import com.dknapik.flowershop.services.administration.AccountAdministrationService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/order")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    ResponseEntity<AccountEmployeeDetailsDTO> retrieveEmployeeAccDetailsFromOrder(
            @Valid @RequestParam("id") UUID orderID) {
        log.traceEntry();

        //service.retrieveAccount()

        log.traceExit();;
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
