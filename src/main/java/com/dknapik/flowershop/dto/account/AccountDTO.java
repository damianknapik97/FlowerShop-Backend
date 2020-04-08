package com.dknapik.flowershop.dto.account;

import com.dknapik.flowershop.dto.DTO;
import com.dknapik.flowershop.model.AccountRole;
import com.dknapik.flowershop.model.order.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
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
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 8)
    @Pattern(regexp = "(?=.*?[0-9])(?=.*?[A-Z]).+")
    private String password;
    @NotNull
    private AccountRole role = AccountRole.ROLE_USER;
    private ShoppingCart shoppingCartList;


    public AccountDTO(String name,
                      String email,
                      String password,
                      AccountRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public AccountDTO(UUID id,
                      String name,
                      String email,
                      String password,
                      AccountRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
