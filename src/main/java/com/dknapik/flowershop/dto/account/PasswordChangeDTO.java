package com.dknapik.flowershop.dto.account;

import com.dknapik.flowershop.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDTO implements DTO {
    private UUID id;
    @NotBlank
    private @Valid String currentPassword;
    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
    private @Valid String newPassword;
    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
    private @Valid String newPasswordConfirmation;

}
