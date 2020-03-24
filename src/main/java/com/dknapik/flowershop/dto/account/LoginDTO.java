package com.dknapik.flowershop.dto.account;

import com.dknapik.flowershop.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Used for user authentication
 *
 * @author Damian
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO implements DTO {
    private String username;
    private String password;
}
