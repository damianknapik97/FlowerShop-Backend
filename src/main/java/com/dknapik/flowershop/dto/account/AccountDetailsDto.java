package com.dknapik.flowershop.dto.account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Transports all non authentication essential account informations
 *
 * @author Damian
 */
public class AccountDetailsDto {
    @NotBlank
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	@Override
	public String toString() {
		return "AccountDetailsDto{" +
				"email='" + email + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AccountDetailsDto that = (AccountDetailsDto) o;

		return email != null ? email.equals(that.email) : that.email == null;
	}

	@Override
	public int hashCode() {
		return email != null ? email.hashCode() : 0;
	}
}
