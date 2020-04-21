package com.dknapik.flowershop.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Class used to transport to information about any account in the system.
 * Details in this class are essential for Employee clearance level users, and should be only accessible by them.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEmployeeDetailsDTO {
    @NotBlank
    private String accountName;
    @NotBlank
    @Email
    private String email;
}
