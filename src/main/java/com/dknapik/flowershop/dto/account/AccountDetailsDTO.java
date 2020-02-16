package com.dknapik.flowershop.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Transports all non authentication essential account informations
 *
 * @author Damian
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsDTO {
    @NotBlank
    @Email
    private @Valid String email;
}
