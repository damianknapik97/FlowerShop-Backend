package com.dknapik.flowershop.viewmodel.account;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AccountDetailsViewModel {
	
	private UUID id;
	
	@NotBlank
	@Email
	private String email;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
