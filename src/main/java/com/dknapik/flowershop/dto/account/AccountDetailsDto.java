package com.dknapik.flowershop.dto.account;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
	
	
}
