package com.dknapik.flowershop.dto.account;

import com.dknapik.flowershop.dto.DTO;
import com.dknapik.flowershop.model.AccountRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountAdministrativeDetailsDTO implements DTO {
    private UUID id;
    private String name;
    private String password;
    private String email;
    private AccountRole role;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime creationDate;

}
