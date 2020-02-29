package com.dknapik.flowershop.dto.account;

import com.dknapik.flowershop.dto.DTO;
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
public class AccountDetailsDTO implements DTO {
    @NotBlank
    @Email
    private @Valid String email;
}
