package com.dknapik.flowershop.dto.account;

import com.dknapik.flowershop.model.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Used for new account creation
 *
 * @author Damian
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private UUID id;
    @NotEmpty
    @Size(min = 4)
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 8)
    @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
    private String password;
    private AccountRole role = AccountRole.USER;

    public AccountDto(@NotEmpty @Size(min = 4) String name,
                      @NotEmpty @Email String email,
                      @NotEmpty @Size(min = 8) @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+") String password,
                      AccountRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
