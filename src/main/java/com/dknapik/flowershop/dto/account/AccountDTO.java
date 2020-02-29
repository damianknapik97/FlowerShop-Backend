package com.dknapik.flowershop.dto.account;

import com.dknapik.flowershop.dto.DTO;
import com.dknapik.flowershop.model.AccountRole;
import com.dknapik.flowershop.model.order.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
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
public class AccountDTO implements DTO {
    private UUID id;
    @NotEmpty
    @Size(min = 4)
    private @Valid String name;
    @NotEmpty
    @Email
    private @Valid String email;
    @NotEmpty
    @Size(min = 8)
    @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
    private @Valid String password;
    private AccountRole role = AccountRole.USER;
    private ShoppingCart shoppingCartList;


    public AccountDTO(@Valid @NotEmpty @Size(min = 4) String name,
                      @Valid @NotEmpty @Email String email,
                      @Valid @NotEmpty @Size(min = 8) @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+") String password,
                      AccountRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public AccountDTO(UUID id,
                      @Valid @NotEmpty @Size(min = 4) String name,
                      @Valid @NotEmpty @Email String email,
                      @Valid @NotEmpty @Size(min = 8) @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+") String password,
                      AccountRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
